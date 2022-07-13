package com.devS.dsmeta.service;

import com.devS.dsmeta.model.Sale;
import com.devS.dsmeta.repository.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsService {

    @Value("${twilio.sid}")
    private String twilioSid;

    @Value("${twilio.key}")
    private String twilioKey;

    @Value("${twilio.phone.from}")
    private String twilioPhoneFrom;

    @Value("${twilio.phone.to}")
    private String twilioPhoneTo;

    @Autowired
    private SaleRepository repository;

    public void sendSms(Long saleId) {

        Optional<Sale> sale = repository.findById(saleId);

        String date = sale.get().getDate().getMonth() + "/" + sale.get().getDate().getYear();

        String msg = "O vendedor " + sale.get().getSellerName() + " foi destaque em " + date + " com um total de R$ "
                + String.format("%.2f", sale.get().getAmount());

        Twilio.init(twilioSid, twilioKey);

        PhoneNumber to = new PhoneNumber(twilioPhoneTo);
        PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

        Message message = Message.creator(to, from, msg).create();

        System.out.println(message.getSid());
    }
}