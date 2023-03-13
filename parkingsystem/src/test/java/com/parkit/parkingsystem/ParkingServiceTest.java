package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService; 

    @Mock
    private static InputReaderUtil inputReaderUtil; 
    @Mock
    private static ParkingSpotDAO parkingSpotDAO; 
    @Mock
    private static TicketDAO ticketDAO; 
    
    @BeforeEach
    private void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            lenient().when (inputReaderUtil.readSelection()).thenReturn (1);
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket(); 
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test // test à compléter de l'étape 5
    public void processExitingVehicleTest(){
        when(ticketDAO.getNbTicket(anyString())).thenReturn(3);
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getNbTicket(anyString());
     } 

    @Test // Test 1 étape 5
    public void testProcessIncomingVehicle(){
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService.processIncomingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class)); 
    }
  
    @Test //Test 2 étape 5
    public void processExitingVehicleTestUnableUpdate() {
      lenient().when (ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
      verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
    }

    @Test // Test 3 étape 5 
    public void testGetNextParkingNumberIfAvailable (){
      lenient().when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
      ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable ();
      assertEquals(1, parkingSpot.getId());
	    assertTrue(parkingSpot.isAvailable());
    }

    @Test // Test 4 étape 5 
    public void  testGetNextParkingNumberIfAvailableParkingNumberNotFound (){
      when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);        
      assertNull(parkingService.getNextParkingNumberIfAvailable());
    }

    @Test // Test 5 étape 5 
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument () {
      lenient().when (inputReaderUtil.readSelection()).thenReturn(3);
      assertNull(parkingService.getNextParkingNumberIfAvailable());
    }

}

