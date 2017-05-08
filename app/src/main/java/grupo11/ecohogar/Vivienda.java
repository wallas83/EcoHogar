package grupo11.ecohogar;

/**
 * Created by Clau on 23/04/2017.
 */

public class Vivienda {
    protected String zona;
    protected long id;

    public Vivienda(String zona, long id) {
        this.zona = zona;
        this.id = id;
    }

    public String getZona() {return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
