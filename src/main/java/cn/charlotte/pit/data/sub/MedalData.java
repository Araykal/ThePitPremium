package cn.charlotte.pit.data.sub;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.*;

/**
 * @Creator Misoryan
 * @Date 2021/6/9 15:26
 */
@Data
public class MedalData {

    //tier medal -> medalName#medalLevel
    //challenged medal have only 1 level , tiered level have more
    private Map<String, MedalStatus> medalStatus;

    public MedalData() {
        this.medalStatus = new HashMap<>();
    }

    @JsonIgnore
    public MedalStatus getMedalStatus(String medalInternal, int level) {
        return getMedalStatus(medalInternal + "#" + level);
    }

    @JsonIgnore
    public Map<String, MedalStatus> getUnlockedMedals() {
        Map<String, MedalStatus> unlockedMedals = new HashMap<>(medalStatus);
        List<String> removeList = new ArrayList<>();
        for (String medal : unlockedMedals.keySet()) {
            if (!unlockedMedals.get(medal).isUnlocked()) {
                removeList.add(medal);
            }
        }
        removeList.forEach(unlockedMedals::remove);
        return unlockedMedals;
    }


    @JsonIgnore
    public MedalStatus getMedalStatus(String medalInternal) {
        if (!medalStatus.containsKey(medalInternal)) {
            return medalStatus.get(medalInternal + "#" + getMedalLevel(medalInternal));
        } else {
            return medalStatus.get(medalInternal);
        }
    }

    @JsonIgnore
    public int getMedalLevel(String medalInternal) {
        List<Integer> medals = new ArrayList<>();
        medals.add(1);
        for (String medal : medalStatus.keySet()) {
            if (medal.startsWith(medalInternal + "#") && medalStatus.get(medal).isUnlocked()) {
                medals.add(Integer.parseInt(medal.replace(medalInternal + "#", "")));
            }
        }
        return Collections.max(medals);
    }


    @Data
    public static class MedalStatus {
        private boolean unlocked;
        private int progress;
        private long finishedTime;
    }

}
