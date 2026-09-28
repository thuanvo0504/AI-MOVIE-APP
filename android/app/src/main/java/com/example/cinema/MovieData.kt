package com.example.cinema

import com.example.cinema.model.Movie

object MovieData {

    val movies = listOf(

        // =========================
        // ĐANG CHIẾU
        // =========================

        Movie(
                id = 1,
                title = "KHÔNG CÒN CHÚNG TA (CHIẾU LẠI)",

                poster = R.drawable.kcct,

                duration = 114,

                // ⭐ ĐÁNH GIÁ PHIM
                rating = 8.5,

                genre = "Tâm Lý • Tình cảm",

                // PHÂN LOẠI KIỂM DUYỆT
                ageRating = "T13",

                certificationDescription =
                    "T13 - PHIM ĐƯỢC PHỔ BIẾN ĐẾN NGƯỜI XEM TỪ ĐỦ 13 TUỔI TRỞ LÊN (13+)",

                description =
                    "Jeong-won và Eun-ho yêu nhau chân thành và sâu đậm bằng những tất cả những gì họ có ở khoảng thời gian đẹp nhất của thanh xuân. Nhưng tình yêu của tuổi trẻ không tránh khỏi sự non nớt, bồng bột để rồi họ chọn rời xa nhau. Nhiều năm sau gặp lại, họ nhận ra: chính những tháng năm đã yêu và đã đau ấy đã giúp họ trưởng thành hơn, hiểu được tình yêu và biết cách yêu thương hơn. Chỉ tiếc rằng khi ấy, yêu thương này họ không dành cho nhau được nữa…",

                director =
                    "Kim Do-Young",

                actors =
                    "Koo Kyo-hwan, Moon Ga-young, Suyeon Ji",

                language =
                    "Tiếng Hàn – Phụ đề tiếng Việt",

                releaseDate =
                    "21/08/2026",

                status =
                    "NOW_SHOWING"

            ),

        Movie(
            id = 2,
            title = "PHIM ĐIỆN ẢNH THÁM TỬ LỪNG DANH CONAN: THIÊN THẦN SA NGÃ TRÊN XA LỘ",
            poster = R.drawable.poster_conan_movie_29,
            duration = 109,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.2,

            genre = "Bí ẩn, Hành Động, Hoạt Hình",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T13",

            certificationDescription =
                "T13 - PHIM ĐƯỢC PHỔ BIẾN ĐẾN NGƯỜI XEM TỪ ĐỦ 13 TUỔI TRỞ LÊN (13+)",

            description =
                "Thám tử nhí Conan, gia đình Ran, Sonoko, đội thám tử nhí cùng Sera Masumi đến khu vực Minatomirai, Yokohama để tham dự “Lễ hội Moto Kanagawa”. Thế nhưng, sự kiện náo nhiệt này nhanh chóng bị khuấy động khi một tay lái bí ẩn mang biệt danh “Quái Xế Đen” bất ngờ xuất hiện. Hắn phóng xe với tốc độ kinh hoàng, lao vọt lên nóc chiếc xe chở nhóm Conan rồi biến mất trong chớp mắt, khiến ngay cả nữ cảnh sát giao thông nổi tiếng với danh xưng “Nữ Thần Gió” của Sở Cảnh sát Kanagawa - Hagiwara Chihaya - cũng không thể truy đuổi kịp. Trong khi cuộc săn lùng vẫn rơi vào bế tắc, tại hội trường lễ hội lại diễn ra màn ra mắt mẫu moto công nghệ cao màu trắng mang tên “Angel”. Điều đáng ngờ là chiếc xe của “Quái Xế Đen” lại sở hữu thiết kế gần như giống hệt mẫu xe này. Bỗng dưng, nữ cảnh sát Chihaya chợt nhớ về những ký ức tưởng chừng đã ngủ yên về người em trai quá cố Hagiwara Kenji và người bạn thân cùng khóa của anh, Matsuda Jinpei. Danh tính thực sự của kẻ cầm lái và mục đích đằng sau những cuộc rượt đuổi đầy nguy hiểm ấy kéo Conan và nữ cảnh sát Chihaya vào một cuộc đấu trí, khơi dậy một vụ án của nhiều năm trước.",

            director =
                "Hasui Takahiro",

            actors =
                "Minami Takayama, Wakana Yamazaki, Rikiya Koyama, Miyuki Sawashiro, Shin-ichiro Miki,..",

            language =
                " Tiếng Nhật – phụ đề Tiếng Việt; Lồng tiếng",

            releaseDate =
                "24/07/2026",

            status =
                "NOW_SHOWING"
        ),
Movie (
        id = 3,
        title = "MINIONS & QUÁI VẬT",
        poster = R.drawable.minion,
        duration = 90,
        // ⭐ ĐÁNH GIÁ PHIM
        rating = 9.0,

        genre = "Hoạt Hình",

        // PHÂN LOẠI KIỂM DUYỆT
        ageRating = "P",

        certificationDescription =
            "P - Phim được phép phổ biến đến người xem ở mọi độ tuổi.",

        description =
            "Minions & Quái Vật là câu chuyện vừa náo loạn, vừa ngớ ngẩn nhưng “hoàn toàn có thật” về cách Minions chinh phục Hollywood, trở thành ngôi sao điện ảnh, rồi mất tất cả, vô tình thả quái vật ra khắp thế giới và sau đó phải cùng nhau hợp sức để cứu lấy hành tinh khỏi chính mớ hỗn loạn mà mình tạo ra.",

        director =
            "Pierre Coffin",

        language =
            " Phụ đề Tiếng Việt & Lồng Tiếng Việt",

        releaseDate =
            "01/07/2026",

        status =
            "NOW_SHOWING"
        ),
        Movie (
            id = 4,
            title = "NGÀY TÀN CỦA PHỐ OAK",
            poster = R.drawable.oak,
            duration = 100,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 8.9,

            genre = "Hành Động, Hồi hộp, Phiêu Lưu",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T13",

            certificationDescription =
                "T13 - Phim được phổ biến đến người xem từ đủ 13 tuổi trở lên (13+)",

            description =
                "Sau khi một thảm họa thiên nhiên xé toạc Phố Oak khỏi khu ngoại ô và đưa con phố đến một nơi xa lạ, gia đình Platt nhanh chóng nhận ra rằng chỉ bằng cách luôn đứng bên nhau, họ mới có thể vượt qua vận mệnh nghiệt ngã này.",

            director =
                "David Robert Mitchell",
            actors =
                "Anne Hathaway, Ewan McGregor, Maisy Stella, Christian Convery,...",

            language =
                " Tiếng Anh - Phụ đề Tiếng Việt",

            releaseDate =
                "14/08/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie (
            id = 5,
            title = "NGÀY TÀN CỦA PHỐ OAK",
            poster = R.drawable.theodyssey,
            duration = 173,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 8.5,

            genre = "Hành Động, Thần thoại",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T16",

            certificationDescription =
                "T16 - Phim được phổ biến đến người xem từ đủ 16 tuổi trở lên (16+)",

            description =
                "Bộ phim tiếp theo của Christopher Nolan, The Odyssey, là một sử thi hành động mang màu sắc thần thoại, được quay tại nhiều địa điểm trên khắp thế giới bằng công nghệ phim IMAX® hoàn toàn mới. Bộ phim lần đầu tiên đưa thiên sử thi kinh điển của Homer lên màn ảnh IMAX®, và sẽ khởi chiếu tại các rạp trên toàn thế giới vào ngày 17 tháng 7 năm 2026.",

            director =
                "Christopher Nolan",
            actors =
                "Benny Safdie, Anne Hathaway, Matt Damon, Robert Pattinson, Tom Holland, Zendaya, Charlize Theron…",

            language =
                " Tiếng Anh - Phụ đề Tiếng Việt",

            releaseDate =
                "17/07/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie (
            id = 6,
            title = "NGƯỜI NHỆN: KHỞI ĐẦU MỚI",
            poster = R.drawable.spiderman2026,
            duration = 145,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.2,

            genre = "Hành Động, Phiêu Lưu, Thần thoại",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T13",

            certificationDescription =
                " T13 - Phim được phổ biến đến người xem từ đủ 13 tuổi trở lên (13+)",

            description =
                "Không còn Tony Stark, MJ hay Ned kề cận, Peter buộc phải đơn thân độc mã đối diện với phe đối đầu bí ẩn. Tuy nhiên, khi áp lực ngày càng gia tăng, nó kích hoạt một sự biến đổi thể chất bất ngờ, đe dọa chính sự tồn tại của anh. Đồng thời, một chuỗi tội phạm bí ẩn mới xuất hiện, kéo theo một trong những mối đe dọa mạnh mẽ nhất mà Spider-Man từng đối mặt.",

            director =
                "Destin Daniel Cretton",
            actors =
                "Tom Holland, Zendaya, Sadie Sink",

            language =
                "  Tiếng Anh - Phụ đề Tiếng Việt",

            releaseDate =
                "31/07/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie (
            id = 7,
            title = "PAW PATROL: PHIM KHỦNG LONG",
            poster = R.drawable.paw,
            duration = 89,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.0,

            genre = "Hành Động, Hoạt Hình, Phiêu Lưu",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "P",

            certificationDescription =
                " P - Phim được phép phổ biến đến người xem ở mọi độ tuổi.",

            description =
                "Khi con tàu của Paw Patrol bị cuốn vào một cơn bão bí ẩn, đội cún cứu hộ vô tình lưu lạc đến một hòn đảo nhiệt đới vô danh, nơi sinh sống của nhiều loài khủng long cổ xưa. Họ gặp Rex, một chuyên gia về khủng long, đã mắc kẹt trên hòn đảo này trong nhiều năm. Mọi chuyện trở nên tệ hơn khi Thị trưởng Humdinger, đối thủ không đội trời chung của PAW Patrol rắp tâm khai thác nguồn tài nguyên thiên nhiên tại đây, và vô tình gây ra trận phun trào của một ngọn núi lửa khổng lồ. Đứng trước nguy cơ hòn đảo bị xóa sổ, đội cún cứu hộ phải thực hiện những nhiệm vụ quy mô \"khủng\" hơn bao giờ hết để ngăn chặn Humdinger và cứu các loài khủng long khỏi nạn tuyệt chủng.",

            director =
                "Cal Brunker",
            actors =
                "Cal Brunker & Bob Barlen",

            language =
                "  Phụ đề Tiếng Việt & Lồng Tiếng Việt",

            releaseDate =
                "14/08/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie (
            id = 8,
            title = "QUỶ QUYỆT: RANH GIỚI VÔ ĐỊNH",
            poster = R.drawable.insidiousoutofthefurtherposter,
            duration = 106,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 8.8,

            genre = "Kinh Dị",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T16",

            certificationDescription =
                " T16 - Phim được phổ biến đến người xem từ đủ 16 tuổi trở lên (16+)" ,

            description =
                "Insidious:Quỷ Quyệt: Ranh Giới Vô Định xoay quanh Gemma (Amelia Eve), một bà mẹ trẻ trở về sống trong ngôi nhà thời thơ ấu cùng con gái và phát hiện mình có thể bước vào Cõi Vô Định, nơi giam giữ những linh hồn lạc lối. Nhưng Gemma không chỉ có thể bước vào Cõi Vô Định, cô còn sở hữu năng lực đưa những thực thể từ đó trở về thế giới thực. Khi các thế lực tà ác phát hiện ra sức mạnh này, ranh giới giữa hai thế giới dần sụp đổ, biến thế giới của người sống thành sân chơi của quỷ dữ.",

            director =
                "Jacob Chase",
            actors =
                "Amelia Eve, Island Austin, Lin Shaye, Sam Spruell, Brandon Perea, and Masie Richardson- Sellers",

            language =
                "  Tiếng Anh - Phụ đề Tiếng Việt",

            releaseDate =
                "21/08/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie (
            id = 9,
            title = "TÀU BUÔN NGƯỜI",
            poster = R.drawable.taubuonnguoi,
            duration = 95,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.3,

            genre = "Hành Động, Hồi hộp, Tội phạm",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T18",

            certificationDescription =
                " T18 - Phim được phổ biến đến người xem từ đủ 18 tuổi trở lên (18+)",

            description =
                "Mutiny (Tàu buồn người) kể về Cole Reed do Jason Statham thủ vai, sau khi tận mắt chứng kiến ông chủ tỷ phú của mình bị sát hại và bản thân trở thành kẻ thế thân bị vu oan, Cole Reed (Jason Statham) đã đột nhập lên một con tàu chở hàng để bắt đầu hành trình truy tìm sự thật và trả thù cho vị sếp cũ. Tuy nhiên, chuyến đi đơn độc này lại đẩy anh vào nguy hiểm khi Cole vô tình bóc trần một âm mưu quốc tế quy mô và cực kỳ nguy hiểm.",

            director =
                "Jean-François Richet",
            actors =
                "Jason Statham, Annabelle Wallis, Adrian Lester",

            language =
                "  Tiếng Anh - Phụ đề Tiếng Việt",

            releaseDate =
                "28/08/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie (
            id = 10,
            title = "PHIM SHIN – CẬU BÉ BÚT CHÌ: KỲ KỲ QUÁI QUÁI! KỲ NGHỈ YÊU QUÁI CỦA TỚ",
            poster = R.drawable.shin,
            duration = 101 ,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.4,

            genre = "Hoạt Hình, Thần thoại",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "P",

            certificationDescription =
                " P - Phim được phép phổ biến đến người xem ở mọi độ tuổi.",

            description =
                "Lấy bối cảnh mùa hè tại Akita, bộ phim theo chân Shin và gia đình Nohara trong chuyến về quê đầy háo hức. Tuy nhiên, một sự kiện kỳ lạ đã đưa cả gia đình lạc vào Xứ sở Yêu quái bí ẩn – nơi con người không được phép đặt chân tới. Tại đây, Shin và gia đình phải đối mặt với những thử thách chưa từng có, đồng thời gặp gỡ hàng loạt yêu quái độc đáo, hài hước và đáng yêu. Với sự kết hợp giữa yếu tố phiêu lưu, kỳ ảo, hài hước đặc trưng của Shin-chan cùng những thông điệp gia đình ấm áp, bộ phim hứa hẹn mang đến một hành trình giải trí hấp dẫn cho khán giả ở mọi lứa tuổi trong mùa hè này.",

            director =
                "Masaki Watanabe",
            actors =
                "Yumiko Kobayashi, Miki Narahashi, Toshiyuki Morikawa, Satomi Koorogi",

            language =
                "  Tiếng Nhật – phụ đề Tiếng Việt",

            releaseDate =
                "21/08/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie (
            id = 11,
            title = "SIÊU CHÓ ĐẠP GIÓ ĐÓN LỄ",
            poster = R.drawable.sieuchodapgio,
            duration = 95 ,
            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.1,

            genre = " Gia đình, Hài, Hoạt Hình",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "P",

            certificationDescription =
                " P - Phim được phép phổ biến đến người xem ở mọi độ tuổi.",

            description =
                "Charlie – chú chó cưng đáng yêu của Danny – bất ngờ bị người ngoài hành tinh bắt cóc và trao cho những siêu năng lực đặc biệt. Nhưng Charlie không phải là “siêu thú” duy nhất trong khu phố khi Puddy – chú mèo tinh quái nhà bên – cũng sở hữu những năng lực phi thường. Cuộc đối đầu giữa hai “siêu thú” mở ra hàng loạt tình huống hài hước, bất ngờ và đầy thú vị, đồng thời mang đến những khoảnh khắc đáng yêu về tình bạn, tình cảm gia đình và tình yêu dành cho các loài vật. Liệu Charlie có thể làm chủ sức mạnh mới và vượt qua những thử thách đang chờ đợi phía trước?",

            director =
                "Shea Wageman",
            actors =
                "Owen Wilson, Vincent Tong, Tabitha St. Germain",

            language =
                "  Tiếng Anh - Lồng tiếng Việt",

            releaseDate =
                "28/08/2026",

            status =
                "NOW_SHOWING"
        ),
        Movie(
            id = 12,
            title = "HÀNH TRÌNH BẤT HẢO",
            poster = R.drawable.hanhtrinhbathao,
            duration = 111,

            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.0,

            genre = "Hài, Hành Động, Tội phạm",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T18",

            certificationDescription = "T18 - Phim được phổ biến đến người xem từ đủ 18 tuổi trở lên (18+)",

            description = "Bị bóp nghẹt bởi nỗi đau mất đi đứa con gái út, bốn mẹ con quyết tâm thực hiện kế hoạch bắt cóc kẻ giết người vừa được tại ngoại sau 8 năm thụ án. Chuyến du lịch ‘chữa lành’ sẽ là chứng cứ ngoại phạm hoàn hảo hay sẽ là hành trình đầy sóng gió cho những “sát thủ nghiệp dư”?",

            director = "KIM MI-JO",

            actors = "LEE JUNG-EUN, KONG HYO-JIN, PARK SO-DAM & LEE YEON",

            language = "Tiếng Hàn với phụ đề Tiếng Việt",

            releaseDate = "04/09/2026",

            status = "NOW_SHOWING"
        ),
        Movie(
            id = 13,
            title = "",
            poster = R.drawable.ono_teaser_470x700,
            duration = 103,

            // ⭐ ĐÁNH GIÁ PHIM
            rating = 8.7,

            genre = "Hài, Tình Cảm",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T18",

            certificationDescription = "T18 - Phim được phổ biến đến người xem từ đủ 18 tuổi trở lên (18+)",

            description = "Sẽ ra sao nếu một đêm hỗn độn lại trở thành điều tuyệt vời nhất từng xảy ra trong đời bạn? Allie và Owen, hai kẻ xa lạ đang \"đói khát\" tình yêu, vô tình va vào nhau giữa lòng New York phiên bản được hư cấu đôi chút, vào đúng đêm duy nhất trong năm hội độc thân được phép \"làm chuyện ấy\". Chàng trai Owen vừa bị đá và cô nàng Allie mộng mơ có lẽ là hai kẻ độc thân duy nhất trong thành phố đang tìm kiếm điều gì đó ý nghĩa hơn là một cuộc tình chóng vánh. Cả hai dường như đã rung động ngay từ lần đầu gặp gỡ, nhưng hàng loạt tình huống dở khóc dở cười cùng những rắc rối phát sinh càng khiến đêm hôm đó trở nên phức tạp và đẩy họ ra xa nhau. Trong lúc mải miết đuổi theo rồi lại vuột mất nhau khắp thành phố, Owen và Allie dần nhận ra rằng, có lẽ điều bản thân khao khát nhất thực chất lại ở gần hơn họ tưởng",

            director = "Will Gluck",

            actors = "Monica Barbaro, Callum Turner, Maya Hawke, Julia Fox, King Princess, Ziwe, Ben Marshall, Ella Loudon with Molly Ringwald and Levar Burton",

            language = "Tiếng Anh với phụ đề tiếng Việt",

            releaseDate = "04/09/2026",

            status = "NOW_SHOWING"
        ),
        Movie(
            id = 14,
            title = "",
            poster = R.drawable.matu,
            duration = 104,

            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.0,

            genre = "Hài, Kinh Dị",
            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T18",

            certificationDescription = "T18 - Phim được phổ biến đến người xem từ đủ 18 tuổi trở lên (18+)",

            description = "Một tù nhân bí ẩn xuất hiện trong một nhà tù khét tiếng bạo lực nhất Indonesia, mang theo một thực thể siêu nhiên chỉ săn lùng những kẻ có năng lượng tiêu cực. Để sống sót, các tù nhân buộc phải cùng nhau làm việc thiện nhằm xóa bỏ bóng tối trong tâm hồn. Hoặc chết từng người một.",

            director = "Joko Anwar",

            actors = "Abimana Aryasatya, Almanzo Konoralma, Aming Sugandhi,...",

            language = "Phụ đề Tiếng Anh - Lồng Tiếng Việt",

            releaseDate = "04/09/2026",

            status = "NOW_SHOWING"
        ),
        Movie(
            id = 15,
            title = "HỘ LINH TRÁNG SĨ - Bí Ẩn Mộ Vua Đinh",
            poster = R.drawable.holinhtrangsi,
            duration = 155,

            // ⭐ ĐÁNH GIÁ PHIM
            rating = 9.6,

            genre = "Hành Động",

            // PHÂN LOẠI KIỂM DUYỆT
            ageRating = "T13",

            certificationDescription = "T13 - Phim được phổ biến đến người xem từ đủ 13 tuổi trở lên (13+)",

            description = "Bộ phim huyền sử về hành trình của 7 Hộ Linh Tráng Sĩ trong nhiệm vụ cuối cùng Tiên Đế giao. Họ phải chạy đua với thời gian, đối đầu với các thế lực thù địch, đưa 99 quan tài của Đinh Tiên Hoàng Đế đi theo 7 hướng khác nhau, đánh lừa kẻ thù đang rắp tâm phá hoại lăng mộ - nơi được tin là cội nguồn vượng khí của dân tộc. Nhưng điều gì ẩn giấu trong những chiếc quan tài ấy? Liệu đó chỉ là thi hài của vị Hoàng đế đầu tiên, hay còn một bí mật lớn lao hơn mà 7 Tráng sĩ đã thề chết để bảo vệ? Hành trình của họ không chỉ là cuộc chiến với kẻ thù bên ngoài, mà còn là cuộc chiến nội tâm, nơi lòng trung thành, tình yêu và sự hy sinh đan xen, tạo nên một khúc ca bi hùng.",

            director = "Nguyễn Phan Quang Bình",

            actors = "Tuấn Trần, Thiên Tú, Johnny Trí Nguyễn, NSND Tự Long, Đỗ Thị Hải Yến, Lê Vũ Long, Quách Ngọc Ngoan, Lê Minh Thuấn, Hứa Vĩ Văn, Diệp Quang Bình, Danis Nguyễn.",

            language = "Tiếng Việt",

            releaseDate = "28/08/2026",

            status = "NOW_SHOWING"
        ),

        // =========================
        // SẮP CHIẾU
        // =========================

        Movie(
            id = 1,
            title = "Mission: Reckoning",
            poster = R.drawable.poster_conan_movie_29,
            duration = 140,
            rating = 8.3,
            genre = "Action • Thriller",
            ageRating = "C13",
            description =
                "Một nhiệm vụ nguy hiểm chống lại trí tuệ nhân tạo.",
            status = "UPCOMING",
            releaseDate = "Sắp khởi chiếu"
        )
    )

    // =========================
    // LẤY PHIM ĐANG CHIẾU
    // =========================

    fun getNowShowingMovies(): List<Movie> {
        return movies.filter {
            it.status.equals(
                "NOW_SHOWING",
                ignoreCase = true
            )
        }
    }

    // =========================
    // LẤY PHIM SẮP CHIẾU
    // =========================

    fun getUpcomingMovies(): List<Movie> {
        return movies.filter {
            it.status.equals(
                "UPCOMING",
                ignoreCase = true
            )
        }
    }
}