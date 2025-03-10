package onboarding;

import java.util.*;
import java.util.stream.Collectors;

public class Problem7 {

    static Map<String, Integer> recommendationScore;
    public static List<String> solution(String user, List<List<String>> friends, List<String> visitors) {
        recommendationScore = new HashMap<>();

        List<String> existingFriends = new ArrayList<>(); //user의 기존 친구 리스트
        List<String> newFriends = new ArrayList<>(); //user의 친구의 친구 리스트

        for(List<String> friend : friends) {
            filterExistFriend(user, friend, existingFriends);
            existingFriends.add(user);
        }

        for(String friend : existingFriends) { //user 친구의 친구 리스트에 추가
            List<String> list = filterNewFriends(friend, user, friends);
            newFriends.addAll(list);
        }

        for (String newFriend : newFriends) { //친구의 친구는 10점 추가
            scoreFriends(newFriend, 10);
        }


        for(String visitor : visitors) { //방문한 횟수 당 1점 추가
            if(existingFriends.contains(visitor)) continue;
            scoreFriends(visitor, 1);
        }

        return selectTop5People(recommendationScore);


    }

    public static void filterExistFriend(String user,List<String> friend, List<String> existingFriendList) {
        if(friend.contains(user)) {
            String friendName = friend.stream()
                    .filter(friendFilter -> !friendFilter.equals(user))
                    .findFirst().orElseThrow();
            existingFriendList.add(friendName);
        }
    }

    public static List<String> filterNewFriends(String friend, String user, List<List<String>> friends) {
        return friends.stream()
                .filter(friendList -> friendList.contains(friend) && !friendList.contains(user))
                .flatMap(Collection::stream)
                .filter(f-> !f.equals(friend))
                .collect(Collectors.toList());
    }

    //점수 추가 로직
    public static void scoreFriends(String account, int point) {
        if(recommendationScore.containsKey(account)) {
            int score = recommendationScore.get(account);
            recommendationScore.replace(account, score + point);
            return;
        }
        recommendationScore.put(account, point);
    }



    public static List<String> selectTop5People(Map<String, Integer> recommendationScore) {
        List<String> answer =
                recommendationScore.entrySet().stream()
                .filter(score -> score.getValue() > 0)
                .sorted((o1, o2) -> {
                    if(o1.getValue().equals(o2.getValue())) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    return o2.getValue() - o1.getValue();
                })
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return answer;
    }
}
