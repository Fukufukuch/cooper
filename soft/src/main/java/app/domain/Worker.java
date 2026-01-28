package app.domain;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import app.entity.WorkerEntity;
import app.domain.Option;

public class Worker {
    private final String id;
    private int monthlyWorkMinutes;
    private int dailyWorkMinutes;    
    private int totalWorkMinutes;
    private boolean isNewcomer;
    private final boolean hasAuthority;
    private final Map<LocalDate, Set<Integer>> assignShiftTimeslotIds;
    private final Set<Integer> availablePositionIds;
    private final Set<Integer> nonconformTags;
    private final Option option;

    public Worker(String id, boolean hasAuthority, int monthlyWorkMinutes, int totalWorkMinutes, 
                  Map<LocalDate, Set<Integer>> assignShiftTimeslotIds, Set<Integer> availablePositionIds, Set<Integer> nonconformTags, Option option) {
        //入力情報
        this.id = id;                                           //労働者ID
        this.hasAuthority = hasAuthority;                       //権限者か
        this.monthlyWorkMinutes = monthlyWorkMinutes;           //月労働時間
        this.totalWorkMinutes = totalWorkMinutes;               //累計労働時間
        this.assignShiftTimeslotIds = assignShiftTimeslotIds;   //シフト希望情報
        this.availablePositionIds = availablePositionIds;       //入れるポジションのID
        this.nonconformTags = nonconformTags;                   //不適合タグ
        this.option = option;                                   //オプション情報

        //入力参照情報
        this.dailyWorkMinutes = 0;                              //日労働時間
        checkIsNewcomer();                                      //新人フラグ判定
    }

    public static Worker fromEntities(WorkerEntity e, Map<LocalDate, Set<Integer>> assignShiftTimeslotIds, 
        Set<Integer> availablePositionIds, Set<Integer> nonconformTags, Option option
    ) {
        boolean hasAuthority = e.getHasAuthority() == 1;
        return new Worker(
            e.getWorkerId(),
            hasAuthority,
            e.getMonthlyWorkMinutes(),
            e.getTotalWorkMinutes(),
            assignShiftTimeslotIds,
            availablePositionIds,
            nonconformTags,
            option
        );
    }

    public String getId() {
        return id;
    }

    public int getMonthlyWorkMinutes() {
        return monthlyWorkMinutes;
    }

    public int getDailyWorkMinutes() {
        return dailyWorkMinutes;
    }

    public int getTotalWorkMinutes() {
        return totalWorkMinutes;
    }

    public Map<LocalDate, Set<Integer>> getAssignShiftTimeslotIds() {
        return assignShiftTimeslotIds;
    }

    public Set<Integer> getAvailablePositionIds() {
        return availablePositionIds;
    }

    public Set<Integer> getNonconformTags() {
        return nonconformTags;
    }

    public boolean isNewcomer() {
        return isNewcomer;
    }

    public boolean isHasAuthority() {
        return hasAuthority;
    }

    // 累計労働時間加算メソッド
    public void addTimes(int time) {
        this.monthlyWorkMinutes += time; //月労働時間加算
        this.dailyWorkMinutes += time;   //日労働時間加算
        this.totalWorkMinutes += time;   //累計労働時間加算
        checkIsNewcomer();
    }

    // 日労働時間リセットメソッド
    public void resetDailyWorkMinutes() {
        this.dailyWorkMinutes = 0;
    }

    // 新人フラグ更新メソッド
    public void checkIsNewcomer() {
        if (this.totalWorkMinutes >= this.option.getNewcomerThresholdMinutes()) { //新人フラグの更新（累計労働時間50時間）
            this.isNewcomer = false;
        } else {
            this.isNewcomer = true;
        }
    }
}