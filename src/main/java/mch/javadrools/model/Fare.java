package mch.javadrools.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Объект представляющий рассчитанную стоимость проезда
 */
public class Fare {

    private Long nightSurcharge; // наценка за ночной тариф
    private Long rideFare;       // стоимость по обычному тарифу

    // итоговая стоимость поездки
    public Long getTotalFare() {
        return nightSurcharge + rideFare;
    }
}
