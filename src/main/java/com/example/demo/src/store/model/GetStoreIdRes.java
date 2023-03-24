package com.example.demo.src.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetStoreIdRes {
    private int storeId;
    public GetStoreIdRes(int storeId){
        this.storeId = storeId;
    }
}
