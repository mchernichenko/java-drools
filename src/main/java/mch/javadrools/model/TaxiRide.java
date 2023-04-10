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
 * Класс с атрибутами, влияющие на тарификацию, т.е.
 * собираемся рассчитать стоимость поездки на основе пройденного расстояния и флага ночной доплаты.
 */
public class TaxiRide {

    private Boolean isNightSurcharge;  // признак использования наценки за ночной тариф
    private Long distanceInMile;       // дистанция

}
