package app.generate;

enum ShortageType {
    SLOT_AUTHORITY,
    POSITION_AUTHORITY,
    WORKER,
    SENIOR;

    @Override
    public String toString() {
        switch (this) {
            case WORKER:
                return "労働者不足";
            case SENIOR:
                return "先輩未割当";
            case POSITION_AUTHORITY:
                return "ポジション管理者不足";
            case SLOT_AUTHORITY:
                return "時間帯管理者不足";
            default:
                return name();
        }
    }
}