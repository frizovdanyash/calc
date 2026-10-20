package com.frizovdanya.vektor.math

data class UnitDefinition(
    val name: String,
    val factor: Double = 1.0,
    val offset: Double = 0.0
) {
    fun toBase(value: Double): Double = value * factor + offset
    fun fromBase(value: Double): Double = (value - offset) / factor
}

data class UnitCategory(
    val name: String,
    val units: List<UnitDefinition>
)

object UnitConverter {
    val categories = listOf(
        UnitCategory(
            "Длина",
            listOf(
                UnitDefinition("Миллиметры · мм", 0.001),
                UnitDefinition("Сантиметры · см", 0.01),
                UnitDefinition("Метры · м"),
                UnitDefinition("Километры · км", 1000.0),
                UnitDefinition("Дюймы · in", 0.0254),
                UnitDefinition("Футы · ft", 0.3048),
                UnitDefinition("Мили · mi", 1609.344)
            )
        ),
        UnitCategory(
            "Масса",
            listOf(
                UnitDefinition("Миллиграммы · мг", 0.000001),
                UnitDefinition("Граммы · г", 0.001),
                UnitDefinition("Килограммы · кг"),
                UnitDefinition("Тонны · т", 1000.0),
                UnitDefinition("Фунты · lb", 0.45359237),
                UnitDefinition("Унции · oz", 0.028349523125)
            )
        ),
        UnitCategory(
            "Температура",
            listOf(
                UnitDefinition("Цельсий · °C"),
                UnitDefinition("Фаренгейт · °F", 5.0 / 9.0, -32.0 * 5.0 / 9.0),
                UnitDefinition("Кельвин · K", 1.0, -273.15)
            )
        ),
        UnitCategory(
            "Площадь",
            listOf(
                UnitDefinition("Квадратные сантиметры · см²", 0.0001),
                UnitDefinition("Квадратные метры · м²"),
                UnitDefinition("Гектары · га", 10000.0),
                UnitDefinition("Квадратные километры · км²", 1000000.0),
                UnitDefinition("Акры · ac", 4046.8564224)
            )
        ),
        UnitCategory(
            "Объём",
            listOf(
                UnitDefinition("Миллилитры · мл", 0.001),
                UnitDefinition("Литры · л"),
                UnitDefinition("Кубические метры · м³", 1000.0),
                UnitDefinition("Чайные ложки · tsp", 0.00492892159375),
                UnitDefinition("Галлоны США · gal", 3.785411784)
            )
        ),
        UnitCategory(
            "Скорость",
            listOf(
                UnitDefinition("Метры в секунду · м/с"),
                UnitDefinition("Км в час · км/ч", 1.0 / 3.6),
                UnitDefinition("Мили в час · mph", 0.44704),
                UnitDefinition("Узлы · kn", 0.514444)
            )
        ),
        UnitCategory(
            "Время",
            listOf(
                UnitDefinition("Секунды · с"),
                UnitDefinition("Минуты · мин", 60.0),
                UnitDefinition("Часы · ч", 3600.0),
                UnitDefinition("Сутки · сут", 86400.0),
                UnitDefinition("Недели · нед", 604800.0)
            )
        ),
        UnitCategory(
            "Данные",
            listOf(
                UnitDefinition("Биты · бит"),
                UnitDefinition("Байты · Б", 8.0),
                UnitDefinition("Килобайты · КБ", 8000.0),
                UnitDefinition("Мегабайты · МБ", 8000000.0),
                UnitDefinition("Гигабайты · ГБ", 8000000000.0),
                UnitDefinition("Кибибайты · КиБ", 8192.0),
                UnitDefinition("Мебибайты · МиБ", 8388608.0)
            )
        )
    )

    fun convert(value: Double, from: UnitDefinition, to: UnitDefinition): Double {
        return to.fromBase(from.toBase(value))
    }
}
