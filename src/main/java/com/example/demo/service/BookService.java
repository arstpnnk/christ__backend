package com.example.demo.service;

import com.example.demo.model.BookLine;
import com.example.demo.repository.BookLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookLineRepository lines;

    public BookService(BookLineRepository lines) {
        this.lines = lines;
    }

    public String getChapterTitle(String bookId, int chapter) {
        if ("bible".equals(bookId) && chapter == 1) return "Книга Бытие. Глава 1";
        if ("prayer_morning".equals(bookId)) return "Утренняя молитва";
        if ("prayer_evening".equals(bookId)) return "Вечерняя молитва";
        if ("prayer_food".equals(bookId)) return "Молитва перед едой";
        if ("psalter".equals(bookId)) return "Псалтирь";
        if ("catechism".equals(bookId)) return "Катехизис";
        if ("apocalypse".equals(bookId)) return "Апокалипсис";
        if ("service".equals(bookId)) return "Служебник";
        return "Книга";
    }

    @Transactional
    public List<String> getChapterLines(String bookId, int chapter) {
        List<BookLine> existing = lines.findByBookIdAndChapterOrderByLineIndex(bookId, chapter);
        if (!existing.isEmpty()) {
            return existing.stream().map(BookLine::getText).collect(Collectors.toList());
        }

        if ("bible".equals(bookId) && chapter == 1) {
            seedGenesis1();
            return lines.findByBookIdAndChapterOrderByLineIndex(bookId, chapter)
                    .stream()
                    .map(BookLine::getText)
                    .collect(Collectors.toList());
        }

        if ("prayer_morning".equals(bookId) && chapter == 1) {
            seedPrayerMorning();
            return lines.findByBookIdAndChapterOrderByLineIndex(bookId, chapter)
                    .stream()
                    .map(BookLine::getText)
                    .collect(Collectors.toList());
        }

        if ("prayer_evening".equals(bookId) && chapter == 1) {
            seedPrayerEvening();
            return lines.findByBookIdAndChapterOrderByLineIndex(bookId, chapter)
                    .stream()
                    .map(BookLine::getText)
                    .collect(Collectors.toList());
        }

        if ("prayer_food".equals(bookId) && chapter == 1) {
            seedPrayerBeforeMeal();
            return lines.findByBookIdAndChapterOrderByLineIndex(bookId, chapter)
                    .stream()
                    .map(BookLine::getText)
                    .collect(Collectors.toList());
        }

        return List.of("Текст будет добавлен позже.");
    }

    private void seedGenesis1() {
        if (lines.existsByBookIdAndChapter("bible", 1)) return;
        List<String> genesis1 = List.of(
                "1. В начале сотворил Бог небо и землю.",
                "2. Земля же была безвидна и пуста, и тьма над бездною, и Дух Божий носился над водою.",
                "3. И сказал Бог: да будет свет. И стал свет.",
                "4. И увидел Бог свет, что он хорош, и отделил Бог свет от тьмы.",
                "5. И назвал Бог свет днем, а тьму ночью. И был вечер, и было утро: день один.",
                "6. И сказал Бог: да будет твердь посреди воды, и да отделяет она воду от воды.",
                "7. И создал Бог твердь, и отделил воду, которая под твердью, от воды, которая над твердью. И стало так.",
                "8. И назвал Бог твердь небом. И был вечер, и было утро: день второй.",
                "9. И сказал Бог: да соберется вода, которая под небом, в одно место, и да явится суша. И стало так.",
                "10. И назвал Бог сушу землею, а собрание вод назвал морями. И увидел Бог, что это хорошо.",
                "11. И сказал Бог: да произрастит земля зелень, траву, сеющую семя, и дерево плодовитое, приносящее по роду своему плод, в котором семя его на земле. И стало так.",
                "12. И произвела земля зелень, траву, сеющую семя по роду ее, и дерево, приносящее плод, в котором семя его по роду его. И увидел Бог, что это хорошо.",
                "13. И был вечер, и было утро: день третий.",
                "14. И сказал Бог: да будут светила на тверди небесной для отделения дня от ночи, и для знамений, и времен, и дней, и годов;",
                "15. и да будут они светильниками на тверди небесной, чтобы светить на землю. И стало так.",
                "16. И создал Бог два светила великие: светило большее, для управления днем, и светило меньшее, для управления ночью, и звезды;",
                "17. и поставил их Бог на тверди небесной, чтобы светить на землю,",
                "18. и управлять днем и ночью, и отделять свет от тьмы. И увидел Бог, что это хорошо.",
                "19. И был вечер, и было утро: день четвертый.",
                "20. И сказал Бог: да произведет вода пресмыкающихся, душу живую; и птицы да полетят над землею, по тверди небесной.",
                "21. И сотворил Бог рыб больших и всякую душу животных пресмыкающихся, которых произвела вода, по роду их, и всякую птицу пернатую по роду ее. И увидел Бог, что это хорошо.",
                "22. И благословил их Бог, говоря: плодитесь и размножайтесь, и наполняйте воды в морях, и птицы да размножаются на земле.",
                "23. И был вечер, и было утро: день пятый.",
                "24. И сказал Бог: да произведет земля душу живую по роду ее, скотов, и гадов, и зверей земных по роду их. И стало так.",
                "25. И создал Бог зверей земных по роду их, и скотов по роду их, и всех гадов земных по роду их. И увидел Бог, что это хорошо.",
                "26. И сказал Бог: сотворим человека по образу Нашему и по подобию Нашему; и да владычествуют они над рыбами морскими, и над птицами небесными, и над скотом, и над всею землею, и над всеми гадами, пресмыкающимися по земле.",
                "27. И сотворил Бог человека по образу Своему, по образу Божию сотворил его; мужчину и женщину сотворил их.",
                "28. И благословил их Бог, и сказал им Бог: плодитесь и размножайтесь, и наполняйте землю, и обладайте ею, и владычествуйте над рыбами морскими, и над птицами небесными, и над всяким животным, пресмыкающимся по земле.",
                "29. И сказал Бог: вот, Я дал вам всякую траву, сеющую семя, какая есть на всей земле, и всякое дерево, у которого плод древесный, сеющий семя; вам сие будет в пищу;",
                "30. а всем зверям земным, и всем птицам небесным, и всякому пресмыкающемуся по земле, в котором душа живая, дал Я всю зелень травную в пищу. И стало так.",
                "31. И увидел Бог всё, что Он создал, и вот, хорошо весьма. И был вечер, и было утро: день шестой."
        );

        List<BookLine> rows = new ArrayList<>();
        for (int i = 0; i < genesis1.size(); i++) {
            BookLine row = new BookLine();
            row.setBookId("bible");
            row.setChapter(1);
            row.setLineIndex(i);
            row.setText(genesis1.get(i));
            rows.add(row);
        }
        lines.saveAll(rows);
    }

    private void seedPrayerMorning() {
        if (lines.existsByBookIdAndChapter("prayer_morning", 1)) return;
        List<String> text = List.of(
                "Инструкция: прочитай молитву спокойно, не спеша. Можно утром после пробуждения.",
                "",
                "Господи, благодарю Тебя за новый день и за жизнь, которую Ты мне даруешь.",
                "Просвети мой ум, укрепи мою волю и направь мои дела к добру.",
                "Сохрани меня от всякого зла, от суеты и раздражения, научи терпению и любви.",
                "Благослови моих близких и всех, кто нуждается в Твоей помощи.",
                "Даруй мне мир в сердце и чистоту помыслов. Аминь."
        );
        saveLines("prayer_morning", 1, text);
    }

    private void seedPrayerEvening() {
        if (lines.existsByBookIdAndChapter("prayer_evening", 1)) return;
        List<String> text = List.of(
                "Инструкция: вечером вспомни день и поблагодари Бога, попроси прощения и мира.",
                "",
                "Господи, благодарю Тебя за прошедший день.",
                "Прости мне всё, в чём я согрешил словом, делом или помышлением.",
                "Если кого обидел — прости и научи меня исправляться.",
                "Покрой меня Твоим милосердием, сохрани в эту ночь и даруй спокойный сон.",
                "Благослови мой дом и близких. Аминь."
        );
        saveLines("prayer_evening", 1, text);
    }

    private void seedPrayerBeforeMeal() {
        if (lines.existsByBookIdAndChapter("prayer_food", 1)) return;
        List<String> text = List.of(
                "Инструкция: перед едой перекрести пищу и поблагодари Бога.",
                "",
                "Господи, благослови эту пищу и питьё во здравие души и тела.",
                "Благодарю Тебя за Твои дары и за заботу о нас. Аминь."
        );
        saveLines("prayer_food", 1, text);
    }

    private void saveLines(String bookId, int chapter, List<String> text) {
        List<BookLine> rows = new ArrayList<>();
        for (int i = 0; i < text.size(); i++) {
            BookLine row = new BookLine();
            row.setBookId(bookId);
            row.setChapter(chapter);
            row.setLineIndex(i);
            row.setText(text.get(i));
            rows.add(row);
        }
        lines.saveAll(rows);
    }
}
