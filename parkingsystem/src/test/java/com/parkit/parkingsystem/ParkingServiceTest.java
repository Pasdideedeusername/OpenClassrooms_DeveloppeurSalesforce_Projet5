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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService; //Carine(pour comprendre): c'est la classe testée

    @Mock
    private static InputReaderUtil inputReaderUtil; //Carine: classe mockée: sert à lire extérieur
    @Mock
    private static ParkingSpotDAO parkingSpotDAO; //Carine(pour comprendre): classe mockée qui sert à chercher le N°parking et modif libre/occupé
    @Mock
    private static TicketDAO ticketDAO; //Carine(pour comprendre): classe mockée qui sert à éditer le ticket (get/save/update)

    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false); //Carine(pour comprendre): la classe ParkingSpot dans model porte uniquement des données en entrée et en sortie, on ne mocke pas !
            Ticket ticket = new Ticket(); //Carine(pour comprendre): classe Ticket comme ParkingSpot dans model
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);//Carine (pour comprendre): indique qu'il y a bien un tiquet mis à jour?
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);//Carine (pour comprendre): indique que parkingSpot is available ?
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test // test à compléter de l'étape 5
    public void processExitingVehicleTest(){
        when(ticketDAO.getNbTicket(anyString())).thenReturn(3);// créé par Carine pour mocker getNbticket()
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getNbTicket(anyString());
     }

    
}
