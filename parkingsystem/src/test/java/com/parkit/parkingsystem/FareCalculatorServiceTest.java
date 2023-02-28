package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.font.TextAttribute;
import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        long currentTime = System.currentTimeMillis(); // nouveau: pour figer le temps dans une variable et ainsi éviter tout écart entre outTime et inTime pour le test
        Date inTime = new Date(currentTime-(60*60*1000)); // On retire une heure
        Date outTime = new Date(currentTime) ;
        
       // inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) ); // code initial de l'exercice
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        long currentTime = System.currentTimeMillis(); // nouveau: pour figer le temps dans une variable et ainsi éviter tout écart de qq ms entre outTime et inTime pour le test
        Date inTime = new Date(currentTime-(60*60*1000)); // On retire une heure
        Date outTime = new Date(currentTime) ;
        
        //Date inTime = new Date();
        //inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        //Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

//    @Test // retiré des tests car expressions lambda non supportées
//    public void calculateFareUnkownType(){
//        long currentTime = System.currentTimeMillis(); // nouveau: pour figer le temps dans une variable et ainsi éviter tout écart de qq ms entre outTime et inTime pour le test
//        Date inTime = new Date(currentTime-(60*60*1000)); // On retire une heure
//        Date outTime = new Date(currentTime) ;
//        
//        //Date inTime = new Date();
//        //inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
//        //Date outTime = new Date();
//        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);
//
//        ticket.setInTime(inTime);
//        ticket.setOutTime(outTime);
//        ticket.setParkingSpot(parkingSpot);
//        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
//    }

//    @Test // retiré des tests car expressions lambda non supportées
//    public void calculateFareBikeWithFutureInTime(){
//        long currentTime = System.currentTimeMillis(); // nouveau: pour figer le temps dans une variable et ainsi éviter tout écart de qq ms entre outTime et inTime pour le test
//        Date inTime = new Date(currentTime+(60*60*1000)); // On rajoute une heure
//        Date outTime = new Date(currentTime) ;
//        
//        //Date inTime = new Date();
//        //inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
//        //Date outTime = new Date();
//        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

//        ticket.setInTime(inTime);
//        ticket.setOutTime(outTime);
//        ticket.setParkingSpot(parkingSpot);
//        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
//    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        long currentTime = System.currentTimeMillis(); // nouveau: pour figer le temps dans une variable et ainsi éviter tout écart de qq ms entre outTime et inTime pour le test
        Date inTime = new Date(currentTime - (45*60*1000)); 
        Date outTime = new Date(currentTime) ;
        
        //Date inTime = new Date();
        //inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        //Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        long currentTime = System.currentTimeMillis(); // nouveau: pour figer le temps dans une variable et ainsi éviter tout écart de qq ms entre outTime et inTime pour le test
        Date inTime = new Date(currentTime - (45*60*1000)); 
        Date outTime = new Date(currentTime) ;
        
        //Date inTime = new Date();
        //inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        //Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    // les 2 tests suivants sont rédigés par Carine dans l'étape 3: implémentez la fonctionnalité des 30 minutes gratuites
    @Test
    public void calculateFareCarWithLessThan30minutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );//30 minutes parking time should give free fare 
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0) , ticket.getPrice());   
    }
    
    @Test
    public void calculateFareBikeWithLessThan30minutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) );//30 minutes parking time should give free fare 
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0) , ticket.getPrice());          
    }

    // Les 2 tests suivants sont rédigés par Carine pour l'étape 4: Développez la fonctionnalité des 5% de remise
    @Test
    public void calculateFareCarWithDiscount(){
        long currentTime = System.currentTimeMillis(); 
        Date inTime = new Date(currentTime - (60*60*1000)); 
        Date outTime = new Date(currentTime) ;
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

       ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, true);
        assertEquals((0.95 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice() );        
    }

// @Test
    public void calculateFareBikeWithDiscount(){
        long currentTime = System.currentTimeMillis(); 
        Date inTime = new Date(currentTime - (60*60*1000)); 
        Date outTime = new Date(currentTime) ;
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, true);
        assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );        
    }
}
