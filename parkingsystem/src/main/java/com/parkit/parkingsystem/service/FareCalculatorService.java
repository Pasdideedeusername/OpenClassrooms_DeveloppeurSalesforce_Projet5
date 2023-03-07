package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    
    public void calculateFare(Ticket ticket, boolean discount){

//    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime(); //.getHours et int changés
        double outHour = ticket.getOutTime().getTime(); //.getHours et int changés

        System.out.println("inhour =" + inHour);
        System.out.println("outhour =" + outHour);

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = ((outHour - inHour)/(60*60*1000)); // int changé pour double et revient en heure
        
        
        //implementation du 30 min gratuites, Etape 3:
        if (duration <= 0.5) { 
            duration = 0;
        }
   
    
    // implémentation du discount pour l'étape 4:
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
 
                break;
            }
            
           default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        if (discount) {
            ticket.setPrice(ticket.getPrice() * 0.95);
        }
    }  
      // rajouté par Carine à l'étape 4. 
    public void calculateFare(Ticket ticket){
        this.calculateFare(ticket, false);
    }

}

