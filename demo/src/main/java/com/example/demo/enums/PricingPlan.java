    package com.example.demo.enums;

    import io.github.bucket4j.Bandwidth;
    import io.github.bucket4j.Refill;

    import java.time.Duration;

    public enum PricingPlan {

        FREE {
            @Override
            public Bandwidth getLimit() {
                return Bandwidth.classic(20, Refill.intervally(20, Duration.ofHours(1)));
            }
        },
        BASIC {
            @Override
            public Bandwidth getLimit() {
                return Bandwidth.classic(40, Refill.intervally(40, Duration.ofHours(1)));
            }
        },
        PROFESSIONAL {
            @Override
            public Bandwidth getLimit() {
                return Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1)));
            }
        };

        public abstract Bandwidth getLimit();


        public static PricingPlan resolvePlanFromApiKey(String apiKey) {
            if (apiKey.startsWith("FX001-")) {
                return FREE;
            } else if (apiKey.startsWith("PX001-")) {
                return PROFESSIONAL;
            } else if (apiKey.startsWith("BX001-")) {
                return BASIC;
            }
            return FREE;
        }
    }
