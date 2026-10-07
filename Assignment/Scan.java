public class Scan {

    enum State {
        IDLE, RUNNING, COMPLETE, CANCELLED
    }

    private int id;
    private String name;
    private int duration;
    private boolean pause;
    private State state;

    public Scan(int id, String name, int duration, boolean pause) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.pause = pause;
        this.state = State.IDLE;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getDuration()
    {
        return duration;
    }

    public boolean isPause()
    {
        return pause;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Scan:" + id + ", " + name + ", "
                + duration + ", "
                + (pause ? "Yes" : "No") + ", "
                + state;
    }

}














