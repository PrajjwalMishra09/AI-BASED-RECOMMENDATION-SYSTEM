import java.util.*;

public class RecommendationSystem {

    // Sample dataset: user -> (item -> rating)
    private static Map<String, Map<String, Integer>> userRatings = new HashMap<>();

    public static void main(String[] args) {
        // Initialize sample data
        userRatings.put("Alice", Map.of("Item1", 5, "Item2", 3, "Item3", 4));
        userRatings.put("Bob", Map.of("Item1", 4, "Item2", 5, "Item4", 2));
        userRatings.put("Charlie", Map.of("Item1", 2, "Item3", 5, "Item4", 4));

        // Recommend for this user
        String targetUser = "Alice";
        List<String> recommendations = getRecommendations(targetUser);

        // Output recommendations
        System.out.println("Recommended items for " + targetUser + ":");
        for (String item : recommendations) {
            System.out.println(" - " + item);
        }
    }

    public static List<String> getRecommendations(String targetUser) {
        Map<String, Integer> targetRatings = userRatings.get(targetUser);
        Map<String, Double> scoreMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        for (String otherUser : userRatings.keySet()) {
            if (otherUser.equals(targetUser)) continue;

            Map<String, Integer> otherRatings = userRatings.get(otherUser);

            // Find similarity score (simple count of common items)
            int similarity = 0;
            for (String item : targetRatings.keySet()) {
                if (otherRatings.containsKey(item)) {
                    similarity++;
                }
            }

            // Add scores of items not rated by targetUser
            for (String item : otherRatings.keySet()) {
                if (!targetRatings.containsKey(item)) {
                    scoreMap.put(item, scoreMap.getOrDefault(item, 0.0) + similarity * otherRatings.get(item));
                    countMap.put(item, countMap.getOrDefault(item, 0) + similarity);
                }
            }
        }

        // Calculate average score
        Map<String, Double> finalScores = new HashMap<>();
        for (String item : scoreMap.keySet()) {
            finalScores.put(item, scoreMap.get(item) / countMap.get(item));
        }

        // Sort and return top items
        List<String> recommendedItems = new ArrayList<>(finalScores.keySet());
        recommendedItems.sort((i1, i2) -> Double.compare(finalScores.get(i2), finalScores.get(i1)));

        return recommendedItems;
    }
}
