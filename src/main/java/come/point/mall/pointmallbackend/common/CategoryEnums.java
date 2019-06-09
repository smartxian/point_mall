package come.point.mall.pointmallbackend.common;

import java.util.HashMap;
import java.util.Map;

public enum  CategoryEnums {
    FUSHI(1000, "服饰鞋包"),
    WENJIAO(1001, "文教用品"),
    CHUFANG(1002, "厨房用具"),
    CHONGWU(1003, "宠物用品"),
    CHENGREN(1004, "成人用品"),
    JIADIAN(1005, "家电数码"),
    YIMEI(1006, "医美健康"),
    JIAFANG(1007, "家纺家饰"),
    MEIZHUANG(1008, "美妆"),
    PEISHI(1009, "配饰"),
    MUYING(1010, "母婴用品"),
    JIAJU(1011, "家具建材"),
    BAIHUO(1012, "百货"),
    SHIPIN(1013, "食品"),
    YUNDONG(1014, "运动户外"),
    QIPEI(1015, "汽配摩托"),
    CHONGZHI(1016, "充值中心"),
    XUNI(1017, "虚拟商品"),
    ;

    private Integer type;

    private String desc;

    private static Map<Integer, CategoryEnums> pool = new HashMap<>();

    static {
        for (CategoryEnums e: CategoryEnums.values()) {
            pool.put(e.getType(), e);
        }
    }

    CategoryEnums(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static String getAdjustTypeDescByType(int type) {
        if (pool.containsKey(type)) {
            return pool.get(type).getDesc();
        }
        return "";
    }
}
