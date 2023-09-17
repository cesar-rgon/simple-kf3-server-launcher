package pojos;

import dtos.MapDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExampleMaps {

    private final MapDto mapDto1;
    private final MapDto mapDto2;
    private final MapDto mapDto3;
    private final MapDto mapDto4;
    private final MapDto mapDto5;
    private final MapDto mapDto6;

    public ExampleMaps() {
        super();
        this.mapDto1 = new MapDto(
                "First map",
                "https://steamuserimages-a.akamaihd.net/ugc/939436531242760934/7DD78A5049EA3DD0BF48963EA11FCE12E629178E/?imw=268&imh=268&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=true",
                LocalDate.of(2018,3,9),
                LocalDateTime.now(),
                "KF-Ashes\n" +
                        "\n" +
                        "credits:\n" +
                        "silent hill konami\n" +
                        "killing floor\n" +
                        "monolith\n" +
                        "\n" +
                        "just stay away from dark alissa..\n" +
                        "havent told you everything,play the map to find out more :)\n" +
                        "\n" +
                        "Update:20/10/18\n" +
                        "added Endless mode Support in array\n" +
                        "new trader kismet sequences(to suit endless mode)\n" +
                        "fixed trader radious\n" +
                        "added trigger on piano @ cathedral traders\n" +
                        "blocking volumes\n" +
                        "fixed expliots\n" +
                        "fixed blood splatts and resolutions\n" +
                        "added triggers ,fixed navigation,kfpathnodes\n" +
                        "fixed physics assets on models\n" +
                        "fixed materails\n" +
                        "rendered some meshes for better optimization\n" +
                        "added random player starts\n" +
                        "\n" +
                        "\n" +
                        "working clock with hands\n" +
                        "5 traders\n" +
                        "events\n" +
                        "custom models\n" +
                        "custom particles and effects\n" +
                        "ashe's and snow shockwave\n" +
                        "custom sounds\n" +
                        "ambiance\n" +
                        "collectabiles\n" +
                        "portals\n" +
                        "teleporters(to traders)\n" +
                        "own boss spawn point\n" +
                        "end credits\n" +
                        "\n" +
                        "original authors and map can be found here:\n" +
                        "http://steamcommunity.com/sharedfiles/filedetails/?\n" +
                        "\n" +
                        "id=302202036&searchtext=ashes\n" +
                        "http://steamcommunity.com/id/Seigneurrat/\n" +
                        "http://steamcommunity.com/id/DannyManPc/\n" +
                        "\n" +
                        "\n" +
                        "good luck and ty for your support",
                false,
                "KF-Ashes",
                false,
                12345678L
        );
        this.mapDto2 = new MapDto(
                "Second map",
                "https://steamuserimages-a.akamaihd.net/ugc/495766099594954649/4531DB07AAD3E141EED990144A4E5D09288A903A/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false",
                LocalDate.of(2016,3,12),
                LocalDateTime.now(),
                "Rev up those fryers!\n" +
                        "\n" +
                        "Zeds have taken over the city beneath Bikini Atoll and only you and your underwater fish friends can stop them!\n",
                true,
                "KF-BikiniAtoll",
                false,
                23456789L
        );
        this.mapDto3 = new MapDto(
                "Third map",
                "https://steamuserimages-a.akamaihd.net/ugc/910157763642009480/3438560E01B662EE605AA25ECE9BAF542C142BBF/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false",
                LocalDate.of(2016,7,28),
                LocalDateTime.now(),
                "img src=\"https://i.imgur.com/zBxs9vb.jpg\" /><br><a class=\"bb_link\" href=\"http://steamcommunity.com/workshop/filedetails/?id=743776156\" target=\"_blank\" rel=\"\" ><img src=\"https://i.imgur.com/TD9Ii7N.jpg\" /></a><br><img src=\"https://i.imgur.com/RYcQ6Nv.jpg\" /><br><b><u>Note:</u></b><br>The Forum link will remain dead while I am in the process of consolidating all my maps into a single mega thread.",
                false,
                "KF-West London",
                false,
                34567890L
        );

        this.mapDto4 = new MapDto(
                "Fourth map",
                "https://wiki.killingfloor2.com/images/a/a6/KF2_Map_Airship.png",
                LocalDate.of(2018,6,12),
                LocalDateTime.now(),
                "Lockhart is back, and it seems his problems are never ending. Last time you saw him, you had helped him launch away to his airship. Now, having followed him, you find the sky a most dangerous place, and not just because you need to help keep the Queen Victoria running!",
                false,
                "KF-Airship",
                true,
                null
        );

        this.mapDto5 = new MapDto(
                "Fith map",
                "https://wiki.killingfloor2.com/images/2/2d/KF2_Map_DieSector.png",
                LocalDate.of(2017,12,12),
                LocalDateTime.now(),
                "This arena was created for the Patriarch to test out his latest and greatest creations, and you’ve somehow found yourself right in the middle of it. It is here that he intends to endlessly subject you to the horde until you succumb. And yes, we do mean endlessly, but only if you can survive of course.",
                true,
                "KF-DieSector",
                true,
                null
        );

        this.mapDto6 = new MapDto(
                "Sixth map",
                "https://wiki.killingfloor2.com/images/b/b5/KF2_Map_MoonBase_Preview.png",
                LocalDate.of(2021,6,22),
                LocalDateTime.now(),
                "Takes place on a secret Horzine research outpost on the Moon which you arrived into via the Steam Powered Rocket. Features low gravity across the map.\n",
                false,
                "KF-MoonBase",
                true,
                null
        );
    }

    public MapDto getMapDto1() {
        return mapDto1;
    }

    public MapDto getMapDto2() {
        return mapDto2;
    }

    public MapDto getMapDto3() {
        return mapDto3;
    }

    public MapDto getMapDto4() {
        return mapDto4;
    }

    public MapDto getMapDto5() {
        return mapDto5;
    }

    public MapDto getMapDto6() {
        return mapDto6;
    }
}
