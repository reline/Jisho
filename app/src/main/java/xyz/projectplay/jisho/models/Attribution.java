package xyz.projectplay.jisho.models;

class Attribution {
    private boolean jmdict;
    private boolean jmnedict;
    private boolean dppedia;

    Attribution() {

    }

    public boolean isJmdict() {
        return jmdict;
    }

    public boolean isJmnedict() {
        return jmnedict;
    }

    public boolean isDppedia() {
        return dppedia;
    }
}
