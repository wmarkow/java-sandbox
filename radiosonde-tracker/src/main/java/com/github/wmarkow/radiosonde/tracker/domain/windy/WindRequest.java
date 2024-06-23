package com.github.wmarkow.radiosonde.tracker.domain.windy;

public class WindRequest
{
    public double lat = 0;
    public double lon = 0;
    public String model = "gfs";
    public String[] parameters =
    { "wind" };
    public String[] levels =
    { "surface", "1000h", "950h", "925h", "900h", "850h", "800h", "700h", "600h", "500h", "400h", "300h",
        "200h", "150h" };
    public String key = "rBD8b2fpPJuzI5a3LCyHaXLXAMa1RDUB";
}
