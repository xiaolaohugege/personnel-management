package com.example.exception;

import lombok.Data;
    /**
     * 自定义异常
     * 运行时异常
     */
    @Data
    public class CustomerException extends RuntimeException {
        private String code;
        private String msg;

        public CustomerException(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public CustomerException(String msg) {
            this.code = "500";
            this.msg = msg;
        }

        public CustomerException() {}

    }

