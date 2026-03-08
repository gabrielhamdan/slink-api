package com.hamdan.slinkapi.entity.user;

public enum EUserRole {

    API_USER,
    ANON_USER;

    public final String TYPE;

    EUserRole() {
        TYPE = String.format("ROLE_%s", name());
    }

}
