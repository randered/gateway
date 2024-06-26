package com.randered.Gateway.bootstrap;

import com.randered.Gateway.entity.Rate;
import com.randered.Gateway.repository.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RatesRepository currencyRateRepository;

    @Override
    public void run(String... args) throws Exception {
        if (currencyRateRepository.count() == 0) {
            loadInitialData();
        }
    }

    private void loadInitialData() {
        final List<Rate> initialRates = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String ratesString = """
                    {"AED":3.92906,"AFN":75.668689,"ALL":100.116424,"AMD":415.545577,
                    "ANG":1.930187,"AOA":915.67391,"ARS":972.631747,"AUD":1.602076,"AWG":1.928157,
                    "AZN":1.825749,"BAM":1.953194,"BBD":2.162215,"BDT":125.832114,"BGN":1.956874,
                    "BHD":0.403224,"BIF":3079.862041,"BMD":1.069713,"BND":1.448853,"BOB":7.400127,
                    "BRL":5.831746,"BSD":1.070871,"BTC":1.741315E-5,"BTN":89.348191,"BWP":14.511859,
                    "BYN":3.504624,"BYR":20966.370565,"BZD":2.15862,"CAD":1.462031,"CDF":3054.030063,
                    "CHF":0.95973,"CLF":0.036486,"CLP":1006.760259,"CNY":7.77328,"CNH":7.806304,
                    "COP":4375.638748,"CRC":560.549492,"CUC":1.069713,"CUP":28.347389,"CVE":110.11808,
                    "CZK":24.89755,"DJF":190.6756,"DKK":7.459759,"DOP":63.235336,"DZD":144.171622,
                    "EGP":51.924245,"ERN":16.045692,"ETB":61.351558,"EUR":1,"FJD":2.421883,
                    "FKP":0.839299,"GBP":0.844542,"GEL":3.005404,"GGP":0.839299,"GHS":16.277283,
                    "GIP":0.839299,"GMD":72.473148,"GNF":9217.744725,"GTQ":8.316904,"GYD":223.96119,
                    "HKD":8.353227,"HNL":26.501518,"HRK":7.507713,"HTG":142.049149,"HUF":396.242635,
                    "IDR":17578.32275,"ILS":4.008236,"IMP":0.839299,"INR":89.405364,"IQD":1402.821791,
                    "IRR":45021.538138,"ISK":149.107344,"JEP":0.839299,"JMD":167.3508,"JOD":0.758106,
                    "JPY":171.11767,"KES":137.725763,"KGS":92.623007,"KHR":4407.078855,"KMF":492.201567,
                    "KPW":962.741646,"KRW":1486.953976,"KWD":0.328049,"KYD":0.892405,"KZT":500.459945,
                    "LAK":23574.43668,"LBP":95899.199063,"LKR":326.95225,"LRD":208.199736,"LSL":19.417502}
                    """;
            initialRates.add(new Rate((long) i, "EUR", ratesString, time.minusHours(i)));
        }
        currencyRateRepository.saveAll(initialRates);
    }

    private final OffsetDateTime time = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
}
