package com.capstone.feminacare.data.remote.response

import android.os.Parcelable
import com.capstone.feminacare.R
import kotlinx.parcelize.Parcelize


@Parcelize
data class ArticleDummy(
    val title: String,
    val imageResources: Int,
    val description: String
) : Parcelable

object Dummy {
    val articles = listOf(
        ArticleDummy("Siklus Menstruasi : Definisi dan Siklus", R.drawable.article1, """
        Siklus menstruasi adalah proses alami yang terjadi dalam sistem reproduksi wanita, melibatkan pelepasan bulanan lapisan rahim. Proses ini juga dikenal sebagai haid, periode menstruasi, atau siklus menstruasi. Siklus menstruasi memiliki empat fase: menstruasi, fase folikuler, ovulasi, dan fase luteal. Fase-fase ini terjadi karena interaksi kompleks hormon dalam tubuh, yang mengatur sistem reproduksi dan mempersiapkannya untuk kehamilan.

        Fase pertama adalah fase menstruasi. Fase ini dimulai ketika telur dari siklus sebelumnya tidak dibuahi, menyebabkan pelepasan lapisan rahim. Fase folikuler mengikuti, yang dimulai pada hari Anda mendapatkan menstruasi. Selama fase ini, hormon merangsang ovarium untuk mulai mengembangkan folikel, masing-masing mengandung satu telur. Fase folikuler berlangsung selama kurang lebih 14 hari.

        Fase ovulasi terjadi ketika folikel dominan melepaskan telur, yang bergerak ke saluran tuba. Fase ini biasanya berlangsung selama 24-48 jam dan diatur oleh hormon seperti hormon luteinisasi dan hormon folikel-stimulasi. Akhirnya, fase luteal dimulai setelah ovulasi dan berlangsung selama kurang lebih 14 hari. Selama fase ini, folikel yang melepaskan telur berubah menjadi korpus luteum, yang menghasilkan progesteron dan mempersiapkan rahim untuk kemungkinan kehamilan. Sepanjang siklus menstruasi, tingkat hormon naik turun, dengan tingkat estrogen naik dan turun dua kali selama siklus. Perubahan hormonal ini memainkan peran penting dalam mengatur siklus menstruasi dan mempersiapkan tubuh untuk kemungkinan kehamilan. Memahami siklus menstruasi dan perubahan hormonal yang terjadi dapat membantu individu mengelola kesehatan reproduksi mereka dengan lebih baik dan mengidentifikasi potensi masalah atau kelainan.

        Sebagai kesimpulan, siklus menstruasi adalah proses alami yang terjadi pada wanita usia reproduksi. Ini melibatkan serangkaian fase yang diatur oleh perubahan hormon dalam tubuh. Memahami siklus menstruasi penting bagi wanita untuk melacak kesuburan dan kesehatan reproduksi secara keseluruhan. Dengan pengetahuan ini, wanita dapat mengambil langkah-langkah untuk mengelola gejala terkait menstruasi dan merencanakan kehamilan atau kontrasepsi. Penting untuk mencari perhatian medis jika ada ketidaknormalan atau kekhawatiran tentang siklus menstruasi.

    """.trimIndent()),
        ArticleDummy("Apa itu Periode “Normal” ?", R.drawable.article2, """
        Siklus menstruasi adalah proses fisiologis alami yang terjadi pada wanita usia reproduksi. Ini merujuk pada perubahan hormonal yang teratur yang mempersiapkan tubuh untuk kehamilan. Meskipun merupakan bagian normal dari kehidupan seorang wanita, dapat terjadi variasi dalam durasi dan karakteristik siklus menstruasi. Memahami apa yang dianggap sebagai periode normal dari siklus menstruasi penting bagi wanita untuk mengenali potensi ketidaknormalan atau masalah kesehatan yang mendasarinya.


        Siklus menstruasi biasanya dimulai dengan timbulnya menstruasi, yang juga dikenal sebagai haid. Ini terjadi ketika lapisan rahim terlepas jika konsepsi tidak terjadi dalam siklus sebelumnya. Durasi haid dapat bervariasi dari wanita ke wanita, berlangsung antara tiga hingga tujuh hari. Sangat normal jika aliran paling banyak pada beberapa hari pertama dan kemudian perlahan-lahan menurun. Rata-rata, seorang wanita kehilangan sekitar 30-40 mililiter darah selama setiap haid.


        Setelah haid berakhir, tubuh mempersiapkan diri untuk kemungkinan kehamilan dengan melepaskan hormon yang dikenal sebagai hormon folikel-stimulasi (FSH) dan hormon luteinizant (LH). Hormon-hormon ini merangsang ovarium untuk melepaskan telur selama ovulasi, yang biasanya terjadi sekitar pertengahan siklus menstruasi. Ovulasi adalah fase 
        penting bagi wanita yang ingin hamil karena menunjukkan peluang kehamilan tertinggi. Setelah telur dilepaskan, itu bergerak melalui saluran tuba menuju rahim.


        Separuh kedua dari siklus menstruasi dikenal sebagai fase luteal. Selama periode ini, hormon progesteron diproduksi untuk mempersiapkan rahim untuk potensi penanaman telur yang telah dibuahi. Jika pembuahan tidak terjadi, tingkat hormon menurun, dan rahim mulai melepaskan lapisannya, menyebabkan dimulainya siklus menstruasi baru.


        Panjang siklus menstruasi normal dapat berkisar antara 24 hingga 38 hari, dengan rata-rata sekitar 28 hari. Penting untuk dicatat bahwa mungkin ada variasi sesekali dalam panjang siklus, terutama selama tahun-tahun remaja ketika fluktuasi hormonal umum terjadi. Stres, penyakit, perubahan berat badan, atau kondisi medis tertentu juga dapat mempengaruhi keteraturan siklus menstruasi.


        Melacak siklus menstruasi dapat membantu mengidentifikasi potensi masalah atau ketidaknormalan. Ini dapat dilakukan dengan menggunakan kalender, aplikasi smartphone, atau alat pelacakan periode khusus. Memantau panjang setiap siklus, durasi dan intensitas haid, dan gejala pendamping seperti kembung, kram, atau perubahan mood dapat memberikan informasi berharga untuk konsultasi dengan profesional kesehatan.


        Meskipun variasi dalam siklus menstruasi umum, penting untuk menyadari tanda-tanda tertentu yang mungkin menunjukkan masalah. Ini termasuk siklus yang sangat tidak teratur, pendarahan yang sangat berat atau berkepanjangan, nyeri hebat, atau perubahan tiba-tiba dalam panjang siklus tanpa penyebab yang jelas. Disarankan untuk mencari saran medis jika muncul salah satu gejala ini untuk menyingkirkan kemungkinan adanya kondisi kesehatan yang mendasari atau ketidakseimbangan hormonal.


        Sebagai kesimpulan, siklus menstruasi normal biasanya berlangsung sekitar 28 hari, meskipun beberapa wanita mungkin mengalami siklus pendek 24 hari atau sesepanjang 38 hari. Periode biasanya berlangsung antara tiga hingga tujuh hari, dengan aliran paling berat pada hari-hari awal. Penting bagi wanita untuk memantau siklus menstruasi mereka untuk mengetahui potensi ketidaknormalan dan mencari saran medis jika mereka mengalami gejala yang parah atau perubahan tiba-tiba yang menyimpang dari pola normal mereka. Memahami apa yang dianggap sebagai siklus menstruasi normal memungkinkan wanita membuat keputusan yang berdasarkan informasi tentang kesehatan reproduksi mereka.

    """.trimIndent()),
        ArticleDummy("Mitos dan Fakta tentang Menstruasi", R.drawable.article3, """
        Menghilangkan Mitos Umum tentang Menstruasi.

        Baru-baru ini, saya menghadiri webinar yang sangat memberi wawasan yang diselenggarakan oleh sayap advokasi perempuan RiSUMSA yang berjudul "Membongkar Mitos Menstruasi." Meskipun merupakan aspek normal dan penting dalam kehidupan, menstruasi sering menghadapi miskonsepsi dan stigma di berbagai masyarakat. Webinar ini secara langsung menanggapi mitos-mitos ini, menyajikan informasi ilmiah yang akurat tentang menstruasi, artinya, dan dampaknya pada individu dan komunitas, mempromosikan diskusi yang terinformasi.

        Studi ini menunjukkan pengetahuan yang kurang memadai di antara responden mengenai informasi terkait menstruasi, menekankan perlunya kesadaran melalui sumber ilmiah seperti sekolah atau profesional kesehatan. Tabu menstruasi mempengaruhi praktik kesehatan perempuan, karena beberapa budaya melarang praktik kebersihan selama menstruasi, yang dapat menyebabkan infeksi. Di beberapa daerah, miskonsepsi tentang mencuci tubuh selama menstruasi dapat menyebabkan infertilitas .

        Lebih lanjut, tabu menstruasi menjadi hambatan signifikan, memengaruhi kemajuan dan kesehatan mental perempuan. Meskipun tidak percaya pada mitos-mitos ini, mereka tetap ada dan harus dihilangkan .

        Hak untuk tidak didiskriminasi dan kesetaraan gender sangat penting, karena stigmatisasi dan norma terkait menstruasi dapat memperkuat praktik diskriminatif. Hambatan terkait menstruasi dalam pendidikan, pekerjaan, layanan kesehatan, dan kegiatan masyarakat juga memperpanjang ketidaksetaraan gender .

        Dampak budaya dan sosial dari menstruasi dapat diatasi melalui percakapan positif. Saya menghargai kesempatan yang diberikan oleh Bolu-Dada Damilola Grace, Basutor Charles Rhema, dan tim mereka untuk belajar dan berkontribusi dalam menghilangkan tabu seputar menstruasi. #PembongkaranMitosMenstruasi #PositivitasHaid #LepaskanTabu .

        Tingkat pengetahuan tentang menstruasi rendah di kalangan responden, menunjukkan miskonsepsi yang dapat mempengaruhi kesehatan reproduksi. Hasil studi ini dapat menjadi titik awal untuk memperkuat kampanye edukasi kesehatan menstruasi, terutama untuk mahasiswa di universitas non-kesehatan. Upaya ini bertujuan untuk membantu remaja membedakan antara pendarahan rahim normal dan pendarahan rahim abnormal .

        Menstruasi adalah bagian dari siklus menstruasi, serangkaian perubahan biologis dalam sistem reproduksi wanita yang mempersiapkan untuk kemungkinan kehamilan. Dipicu oleh hormon, siklus ini dimulai pada masa pubertas dan berlanjut hingga menopause .

        Sebagai kesimpulan, membongkar mitos menstruasi penting untuk mengatasi miskonsepsi masyarakat, meningkatkan pengetahuan, dan mempromosikan kesetaraan gender. Tabu menstruasi yang bertahan mempengaruhi berbagai aspek kehidupan perempuan, dan upaya bersama diperlukan untuk mengatasi hambatan ini dan membangun masyarakat yang lebih terinformasi dan mendukung.

    """.trimIndent())
    )
}


