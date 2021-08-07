package com.cos.mediumclone.util;

public interface InitSettings {

    void init(); // findViewById
    void initLr(); // 리스너 등록
    default void initAdapter(){}; // 어댑터 등록
    default void initNavigation(){};
    default void initSetting(){}; // 기타 셋팅
    void initData(); // 데이터 초기화
}
