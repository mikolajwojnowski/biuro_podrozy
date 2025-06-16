package org.example.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.backend.models.Reservation;
import org.example.backend.models.ReservationParticipant;
import org.example.backend.models.Trip;
import org.example.backend.models.User;
import org.example.backend.repository.ReservationRepository;
import org.example.backend.repository.TripRepository;
import org.example.backend.repository.ReservationParticipantRepository;
import org.example.backend.DTO.ReservationDTO;
import org.example.backend.DTO.ParticipantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.BaseColor;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TripRepository tripRepository;
    private final ReservationParticipantRepository participantRepository;
    private final UserService userService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, 
                            TripRepository tripRepository,
                            ReservationParticipantRepository participantRepository,
                            UserService userService) {
        this.reservationRepository = reservationRepository;
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
        this.userService = userService;
    }

    @Transactional
    public Reservation createReservation(ReservationDTO reservationDTO) {
        Trip trip = tripRepository.findById(reservationDTO.getTripId())
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));

        // Check if there's available capacity
        int numberOfParticipants = reservationDTO.getParticipants().size();
        if (numberOfParticipants > trip.getAvailableSpots()) {
            throw new IllegalStateException("Not enough available spots for this trip");
        }

        // Get the authenticated user from the security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        
        User user = userService.findByEmail(auth.getName());
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        // Create the reservation
        Reservation reservation = new Reservation();
        reservation.setTrip(trip);
        reservation.setUser(user);
        reservation.setName(reservationDTO.getName());
        reservation.setSurname(reservationDTO.getSurname());
        reservation.setEmail(reservationDTO.getEmail());
        reservation.setPhoneNumber(reservationDTO.getPhoneNumber());
        reservation.setNumberOfPeople(numberOfParticipants);
        
        // Calculate total price based on number of participants
        BigDecimal pricePerPerson = BigDecimal.valueOf(trip.getPrice());
        reservation.setTotalPrice(pricePerPerson.multiply(BigDecimal.valueOf(numberOfParticipants)));
        
        // Set active status
        reservation.setActive(true);

        // Save the reservation first to get its ID
        reservation = reservationRepository.save(reservation);

        // Add participants
        for (ParticipantDTO participantDTO : reservationDTO.getParticipants()) {
            ReservationParticipant participant = new ReservationParticipant(
                participantDTO.getName(),
                participantDTO.getSurname()
            );
            reservation.addParticipant(participant);
        }

        // Update trip's available spots
        trip.setAvailableSpots(trip.getAvailableSpots() - numberOfParticipants);
        tripRepository.save(trip);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("User not authenticated");
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        // Allow admins to cancel any reservation, but users can only cancel their own
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
            !reservation.getUser().getEmail().equals(auth.getName())) {
            throw new AccessDeniedException("You can only cancel your own reservations");
        }

        // Update trip's available spots before deactivating
        Trip trip = reservation.getTrip();
        trip.setAvailableSpots(trip.getAvailableSpots() + reservation.getNumberOfPeople());
        tripRepository.save(trip);

        // Deactivate the reservation
        reservation.setActive(false);
        reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReservationsForTrip(Long tripId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("User not authenticated");
        }

        // Return all active reservations for the trip
        return reservationRepository.findByTripIdAndActiveTrue(tripId);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }

    @Transactional
    public void deactivateReservation(Long reservationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Admin access required");
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        // Update trip's available spots before deactivating
        Trip trip = reservation.getTrip();
        trip.setAvailableSpots(trip.getAvailableSpots() + reservation.getNumberOfPeople());
        tripRepository.save(trip);

        reservation.setActive(false);
        reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public byte[] generateReservationPdf(Long reservationId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                throw new AccessDeniedException("User not authenticated");
            }

            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

            System.out.println("Starting PDF generation for reservation ID: " + reservationId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(com.itextpdf.text.PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            // Set document metadata
            document.addTitle("Reservation Details - " + reservation.getId());
            document.addAuthor("Travel Agency System");
            document.addCreator("Travel Agency System");

            // Add content with proper spacing and formatting
            try {
                // Title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
                Paragraph title = new Paragraph("Reservation Details", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

                // Trip Information
                Paragraph tripSection = new Paragraph("Trip Information", sectionFont);
                tripSection.setSpacingBefore(15);
                tripSection.setSpacingAfter(10);
                document.add(tripSection);

                document.add(new Paragraph("Trip: " + reservation.getTrip().getTitle(), contentFont));
                document.add(new Paragraph("Date: " + reservation.getTrip().getTripDate(), contentFont));
                document.add(new Paragraph("Duration: " + reservation.getTrip().getDays() + " days", contentFont));

                // Customer Information
                Paragraph customerSection = new Paragraph("Customer Information", sectionFont);
                customerSection.setSpacingBefore(15);
                customerSection.setSpacingAfter(10);
                document.add(customerSection);

                document.add(new Paragraph("Name: " + reservation.getName() + " " + reservation.getSurname(), contentFont));
                document.add(new Paragraph("Email: " + reservation.getEmail(), contentFont));
                document.add(new Paragraph("Phone: " + reservation.getPhoneNumber(), contentFont));

                // Reservation Details
                Paragraph reservationSection = new Paragraph("Reservation Details", sectionFont);
                reservationSection.setSpacingBefore(15);
                reservationSection.setSpacingAfter(10);
                document.add(reservationSection);

                document.add(new Paragraph("Reservation ID: " + reservation.getId(), contentFont));
                document.add(new Paragraph("Number of People: " + reservation.getNumberOfPeople(), contentFont));
                document.add(new Paragraph("Total Price: $" + reservation.getTotalPrice(), contentFont));
                document.add(new Paragraph("Status: " + (reservation.isActive() ? "Active" : "Cancelled"), contentFont));

                // Participants Table
                Paragraph participantsSection = new Paragraph("Participants List", sectionFont);
                participantsSection.setSpacingBefore(15);
                participantsSection.setSpacingAfter(10);
                document.add(participantsSection);

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setSpacingAfter(10);

                // Style for table headers
                Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
                PdfPCell nameHeader = new PdfPCell(new Phrase("Name", tableHeaderFont));
                PdfPCell surnameHeader = new PdfPCell(new Phrase("Surname", tableHeaderFont));

                nameHeader.setBackgroundColor(new BaseColor(240, 240, 240));
                surnameHeader.setBackgroundColor(new BaseColor(240, 240, 240));
                nameHeader.setPadding(5);
                surnameHeader.setPadding(5);

                table.addCell(nameHeader);
                table.addCell(surnameHeader);

                // Add participants with styled cells
                for (ReservationParticipant participant : reservation.getParticipants()) {
                    PdfPCell nameCell = new PdfPCell(new Phrase(participant.getName(), contentFont));
                    PdfPCell surnameCell = new PdfPCell(new Phrase(participant.getSurname(), contentFont));
                    nameCell.setPadding(5);
                    surnameCell.setPadding(5);
                    table.addCell(nameCell);
                    table.addCell(surnameCell);
                }

                document.add(table);

                // Footer
                Paragraph footer = new Paragraph("Generated on: " + java.time.LocalDateTime.now(),
                        FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
                footer.setSpacingBefore(20);
                footer.setAlignment(Element.ALIGN_RIGHT);
                document.add(footer);

                System.out.println("Content added successfully");
            } catch (Exception e) {
                System.err.println("Error while adding content to PDF: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error adding content to PDF", e);
            }

            document.close();
            writer.close();

            byte[] pdfContent = baos.toByteArray();
            if (pdfContent.length == 0) {
                throw new RuntimeException("Generated PDF is empty");
            }
            System.out.println("PDF generated successfully, size: " + pdfContent.length + " bytes");
            return pdfContent;

        } catch (Exception e) {
            System.err.println("Error in PDF generation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    public List<ReservationParticipant> getParticipants(Long reservationId) {
        return participantRepository.findByReservationId(reservationId);
    }
} 