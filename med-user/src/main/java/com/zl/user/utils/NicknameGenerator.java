package com.zl.user.utils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "Happy", "Clever", "Brave", "Gentle", "Swift",
            "Lucky", "Wise", "Calm", "Bright", "Wild"
    );

    private static final List<String> NOUNS = List.of(
            "Tiger", "Phoenix", "Dragon", "Wolf", "Eagle",
            "Lion", "Fox", "Bear", "Hawk", "Shark"
    );

    private static final Random RANDOM = new Random();

    /**
     * 生成随机昵称（形容词 + 名词 + 随机数）
     * 示例：HappyTiger123
     */
    public static String generateRandomNickname() {
        String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(RANDOM.nextInt(NOUNS.size()));
        int randomNum = RANDOM.nextInt(1000); // 0-999
        return String.format("%s%s%03d", adjective, noun, randomNum);
    }

    /**
     * 基于用户名生成昵称（用户名 + 随机后缀）
     * 示例：john_doe_abc123
     */
    public static String generateNicknameFromUsername(String username) {
        String suffix = IntStream.range(0, 4)
                .mapToObj(i -> Character.toString(RANDOM.nextInt(26) + 'a'))
                .collect(Collectors.joining());
        int randomNum = RANDOM.nextInt(90) + 10; // 10-99
        return String.format("%s_%s%d", username, suffix, randomNum);
    }
}
