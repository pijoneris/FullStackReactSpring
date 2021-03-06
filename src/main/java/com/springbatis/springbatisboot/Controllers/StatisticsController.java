package com.springbatis.springbatisboot.Controllers;


import com.springbatis.springbatisboot.Mappers.PaymentMapper;
import com.springbatis.springbatisboot.Mappers.StatisticsMapper;
import com.springbatis.springbatisboot.Models.Reservation;
import com.springbatis.springbatisboot.Models.ReservationId;
import com.springbatis.springbatisboot.StatiscticsModels.StatiscticsFlightsReqBody;
import com.springbatis.springbatisboot.StatiscticsModels.StatisticsFlightResBody;
import com.springbatis.springbatisboot.StatiscticsModels.StatisticsFlights;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/stats")
public class StatisticsController {


    public StatisticsMapper mapper;
    public PaymentMapper paymentMapper;

    public StatisticsController(StatisticsMapper mapper, PaymentMapper paymentMapper) {
        this.mapper = mapper; this.paymentMapper = paymentMapper;
    }

    @PostMapping("/flights")
    Map<String, ArrayList<StatisticsFlightResBody>> getFlightStats(@RequestBody StatiscticsFlightsReqBody req){
        List<StatisticsFlightResBody> stats = mapper.formStatistics(req);
        if(req.showPayments){
            for(int i = 0; i<stats.size(); i++){
                String k = stats.get(i).reservationId.toString();
                stats.get(i).payments = paymentMapper.selectPaymentByReservationId(new ReservationId(Integer.parseInt(k)));
            }
        }

        Map<String, ArrayList<StatisticsFlightResBody>> rPerFlight = new HashMap<>();
        for (StatisticsFlightResBody res: stats) {
            if(!rPerFlight.containsKey(res.flightNumber)){
                rPerFlight.put(res.flightNumber.toString(), new ArrayList<StatisticsFlightResBody>());
            }
            rPerFlight.get(res.flightNumber.toString()).add(res);
        }



        return rPerFlight;
    }
}
