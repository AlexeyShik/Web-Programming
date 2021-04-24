package ru.itmo.web.lesson4.util;

import ru.itmo.web.lesson4.model.Post;
import ru.itmo.web.lesson4.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", User.Color.BLUE, 1),
            new User(6, "pashka", "Pavel Mavrin", User.Color.RED, 1),
            new User(9, "geranazarov555", "Georgiy Nazarov", User.Color.GREEN, 1),
            new User(11, "tourist", "Gennady Korotkevich", User.Color.RED, 1)
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(1, "EDU: Новое занятие курса \"ITMO Academy: пилотный курс\" — Введение в теорию графов",
                    "Привет!\n" +
                            "\n" +
                            "Новым занятием в разделе EDU мы начинаем цикл, посвященный графовым алгоритмам." +
                            " Это занятие ориентировано на самых начинающих — в нём изложены основные определения и понятия теории графов.\n" +
                            "\n" +
                            "Общий список занятий в \"ITMO Academy: пилотный курс\" теперь выглядит так:\n" +
                            "\n" +
                            "z-функция\n" +
                            "суффиксный массив\n" +
                            "дерево отрезков, часть 1\n" +
                            "дерево отрезков, часть 2\n" +
                            "двоичный поиск\n" +
                            "система непересекающихся множеств\n" +
                            "введение в теорию графов\n" +
                            "Подробнее об учебном подразделе на Codeforces (и его β-тестировании) можно прочитать по ссылке.\n" +
                            "\n" +
                            "Перейти в раздел EDU →\n" +
                            "Большое спасибо le.mur за монтаж видео, а также студентам ИТМО Supermagzzz и Stepavly за подготовку практических задач.\n" +
                            "\n" +
                            "Занятие состоит из 4 шагов:\n" +
                            "\n" +
                            "неориентированные графы (7 задач);\n" +
                            "классы графов (4 задачи);\n" +
                            "ориентированные графы (4 задачи);\n" +
                            "представления графов (4 задачи);\n" +
                            "Надеюсь, что получилось интересно и полезно.\n" +
                            "\n" +
                            "Полный текст »\n" +
                            "\n" +
                            "Теги codeforces, edu, графы\n", 1),
            new Post(2, "touristreams are back! 006: SNSS 2020 R1, and more",
                    "It's this time of the year again!\n" +
                            "\n" +
                            "Tune in at twitch.tv/the__tourist at around 21:45 MSK, 21 Aug 2020 to watch me solving SnarkNews Summer Series 2020 Round 1 live! First I will do the round competitively without much interaction, and I'll be up for discussing the problems on stream afterwards.\n" +
                            "\n" +
                            "SnarkNews Winter/Summer Series (SNWS/SNSS) is organized by snarknews yearly and consists of 5 rounds. Each round is a virtual 6-problem 80-minute contest open for 10 days. The series is open to all, but problem statements are in Russian only. Unlike last series, I'll be reading problem statements automatically translated to English on stream.\n" +
                            "\n" +
                            "To make the stream more fun and for easier understanding of what will be going on, you're welcome to do the round yourself beforehand! If you don't understand Russian, you can also compete using automatic translators. Alternatively, you may just read the problems without solving them. In any case, don't discuss the problems in Codeforces comments before the round window ends.\n" +
                            "\n" +
                            "Tentative schedule of future streams:\n" +
                            "\n" +
                            "Round 2 (contest link): 21:45 MSK, 25 Aug 2020\n" +
                            "Round 3 (contest link): 21:45 MSK, 5 Sep 2020\n" +
                            "Round 4 (contest link): 21:45 MSK, 7 Sep 2020\n" +
                            "Round 5 (contest link): 21:45 MSK, 9 Sep 2020\n" +
                            "I might be streaming at other times too, so be sure to follow me on Twitch and get instant notifications.\n" +
                            "\n" +
                            "See you on stream!\n" +
                            "\n" +
                            "UPD: Round 1 stream is available on my channel: link. Round 2 stream is starting as planned, in 8 hours — be sure to join! Here is the contest link if you'd like to do the contest or read the problems beforehand.\n" +
                            "\n" +
                            "UPD2: Round 2 stream: link. Unfortunately, Round 3 has not started yet — therefore, the next streams have to be postponed. Follow this blog and my Twitch for updates.\n" +
                            "\n" +
                            "UPD3: New dates of Rounds 3-5 (and hence my streams) have been announced!\n" +
                            "\n" +
                            "UPD4: Just a friendly reminder: Round 3 ends in less than 24 hours, and thus my next stream is coming tomorrow! Also note that Rounds 4 and 5 are about to end as well, there are just two days between consecutive rounds.\n" +
                            "\n" +
                            "UPD5: Round 3 stream: link. Round 4 stream is coming in less than 4 hours from now!\n" +
                            "\n" +
                            "UPD6: Round 4 stream: link. The last stream of the series, Round 5, is coming today in less than 3 hours!\n" +
                            "\n" +
                            "UPD7: Round 5 stream: link (with virtual Codeforces Round 669 (Div. 2) as a practice before SNSS). This was the last stream of the series, thank you for tuning in! However, this was not the last stream of the season — check this blog post for more.", 11),
            new Post(3, "ITMO Algorithms Course",
                    "Hello Codeforces!\n" +
                            "\n" +
                            "I teach a course on algorithms and data structures at ITMO University. During the last year I was streaming all my lectures on Twitch and uploaded the videos on Youtube.\n" +
                            "\n" +
                            "This year I want to try to do it in English.\n" +
                            "\n" +
                            "This is a four-semester course. The rough plan for the first semester:\n" +
                            "\n" +
                            "Algorithms, complexity, asymptotics\n" +
                            "Sorting algorithms\n" +
                            "Binary heap\n" +
                            "Binary search\n" +
                            "Linked lists, Stack, Queue\n" +
                            "Amortized analysis\n" +
                            "Fibonacci Heap\n" +
                            "Disjoint Set Union\n" +
                            "Dynamic Programming\n" +
                            "Hash Tables\n" +
                            "The lectures are open for everybody. If you want to attend, please fill out this form to help me pick the optimal day and time.\n" +
                            "\n" +
                            "See you!", 6),
            new Post(4, "Web Practice 5", "Practice will happen 16.10.2020", 9)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        HttpSession session = request.getSession();

        if (request.getRequestURI().contains("/logout")) {
            request.getSession().setAttribute("user", null);
        }

        if (session.getAttribute("user") != null) {
            data.put("user", session.getAttribute("user"));
        }

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
                request.getSession().setAttribute("user", user);
            }
        }
    }
}
