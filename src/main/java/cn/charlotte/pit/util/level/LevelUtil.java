package cn.charlotte.pit.util.level;

import cn.charlotte.pit.util.chat.RomanUtil;

/**
 * @Author: Misoryan
 * @Date: 2020/12/29 22:30
 */

public class LevelUtil {

    public static void main(String[] args) {
        System.out.println(getLevelExpRequired(28, 71));
    }

    /**
     * @param prestige 精通
     * @return 精通的标识颜色
     */

    public static String getPrestigeColor(int prestige) {
        if (prestige >= 1 && prestige <= 4) {
            return "&9";
        } else if (prestige >= 5 && prestige <= 9) {
            return "&e";
        } else if (prestige >= 10 && prestige <= 14) {
            return "&6";
        } else if (prestige >= 15 && prestige <= 19) {
            return "&c";
        } else if (prestige >= 20 && prestige <= 24) {
            return "&5";
        } else if (prestige >= 25 && prestige <= 29) {
            return "&d";
        } else if (prestige >= 30 && prestige <= 34) {
            return "&f";
        } else if (prestige >= 35 && prestige <= 39) {
            return "&b";
        } else if (prestige >= 40 && prestige <= 44) {
            return "&0";
        } else if (prestige >= 45 && prestige <= 47) {
            return "&0";
        } else if (prestige >= 48 && prestige <= 50) {
            return "&4";
        } else if (prestige >= 51 && prestige <= 65) {
            return "&1";
        } else if (prestige >= 66 && prestige <= 70) {
            return "&3";
        } else if (prestige >= 71 && prestige <= 85) {
            return "&2";
        } else if (prestige >= 86 && prestige <= 90) {
            return "&2";
        } else if (prestige >= 91) {
            return "&8";
        }
        return "&7";
    }

    /**
     * @param level 等级
     * @return 等级的标识颜色
     */
    public static String getLevelColor(int level) {
        if (level >= 10) {
            switch ((level - level % 10) / 10) {
                case 1:
                    return "&9";
                case 2:
                    return "&3";
                case 3:
                    return "&2";
                case 4:
                    return "&a";
                case 5:
                    return "&e";
                case 6:
                    return "&6&l";
                case 7:
                    return "&c&l";
                case 8:
                    return "&4&l";
                case 9:
                    return "&5&l";
                case 10:
                    return "&d&l";
                case 11:
                    return "&f&l";
            }
            //>=120
            return "&b&l";
        }
        return "&7";
    }

    /**
     * @param prestige 玩家当前精通等级
     * @param level    玩家当前等级
     * @return 某一等级所需经验值(level)
     */
    public static double getLevelExpRequired(int prestige, int level) {
        double boost = 1.1;
        if (level >= 10) {
            switch ((level - level % 10) / 10) {
                case 1:
                    return Math.round(Math.pow(boost, prestige) * 30);
                case 2:
                    return Math.round(Math.pow(boost, prestige) * 50);
                case 3:
                    return Math.round(Math.pow(boost, prestige) * 75);
                case 4:
                    return Math.round(Math.pow(boost, prestige) * 125);
                case 5:
                    return Math.round(Math.pow(boost, prestige) * 250);
                case 6:
                    return Math.round(Math.pow(boost, prestige) * 600);
                case 7:
                    return Math.round(Math.pow(boost, prestige) * 800);
                case 8:
                    return Math.round(Math.pow(boost, prestige) * 900);
                case 9:
                    return Math.round(Math.pow(boost, prestige) * 1000);
                case 10:
                    return Math.round(Math.pow(boost, prestige) * 1200);
            }
            //>=110
            return Math.round(Math.pow(boost, prestige) * 1500);
        } else {
            // 0~9
            return Math.round(Math.pow(boost, prestige) * 15);
        }
    }

    /**
     * @param prestige 精通等级
     * @param exp      总经验值
     * @return 通过已有经验值推算等级
     */
    public static int getLevelByExp(int prestige, double exp) {
        double experience = exp;
        int level = 0;
        for (int i = 0; i <= 120; i++) {
            level = i;
            if (experience >= getLevelExpRequired(prestige, i)) {
                experience = experience - getLevelExpRequired(prestige, i);
            } else {
                return i;
            }
        }
        return level;
    }

    /**
     * @param prestige
     * @param level
     * @return 获取升级到某一级所需经验值(0 - level)
     */
    public static double getLevelTotalExperience(int prestige, int level) {
        double experience = 0;
        for (int i = 0; i < level; i++) {
            experience = experience + getLevelExpRequired(prestige, i);
        }
        return experience;
    }

    public static float getLevelProgress(int prestige, double experience) {
        int level = LevelUtil.getLevelByExp(prestige, experience);
        if (level >= 120) {
            return 1;
        } else {
            return (float) ((getLevelExpRequired(prestige, level) - getLevelTotalExperience(prestige, level + 1) + experience) / getLevelExpRequired(prestige, level));
        }
    }

    /**
     * @param prestige
     * @param experience
     * @return 通过精通和经验值获得一个展示用等级标志 [120] <- example
     */
    public static String getLevelTag(int prestige, double experience) {
        int level = getLevelByExp(prestige, experience);
        int pre = 120 * prestige / 30;
        if (prestige > 0 && pre < 10) {
            pre = 10;
        }
        return getPrestigeColor(prestige) + "[" + getLevelColor(level) + level + getPrestigeColor(prestige) + "]";
    }


    public static String getLevelTag(int prestige, int level) {
        int pre = 120 * prestige / 30;
        if (prestige > 0 && pre < 10) {
            pre = 10;
        }
        return getPrestigeColor(prestige) + "[" + getLevelColor(level) + level + getPrestigeColor(prestige) + "]";
    }

    public static String getLevelTagWithRoman(int prestige, double experience) {
        if (prestige > 0) {
            int level = getLevelByExp(prestige, experience);
            int pre = 120 * prestige / 30;
            if (pre < 10) {
                pre = 10;
            }
            return getPrestigeColor(prestige) + "[&e" + RomanUtil.convert(prestige) + getPrestigeColor(prestige) + "-" + getLevelColor(level) + level + getPrestigeColor(prestige) + "]";
        } else {
            return getLevelTag(prestige, experience);
        }
    }

    public static String getLevelTagWithRoman(int prestige, int level) {
        if (prestige > 0) {
            int pre = 120 * prestige / 30;
            if (pre < 10) {
                pre = 10;
            }
            return getPrestigeColor(prestige) + "[&e" + RomanUtil.convert(prestige) + getPrestigeColor(prestige) + "-" + getLevelColor(level) + level + getPrestigeColor(prestige) + "]";
        } else {
            return getLevelTag(prestige, level);
        }
    }
}
