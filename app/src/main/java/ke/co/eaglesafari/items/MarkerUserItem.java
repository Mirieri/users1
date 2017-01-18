package ke.co.eaglesafari.items;

import com.google.android.gms.maps.model.Marker;


public class MarkerUserItem {

    Marker marker;
    String id;

    public Marker getMarker() {
        return marker;
    }

    public MarkerUserItem setMarker(Marker marker) {
        this.marker = marker;
      return   this;
    }

    public String getId() {
        return id;
    }

    public MarkerUserItem setId(String id) {

        this.id = id;
        return  this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkerUserItem that = (MarkerUserItem) o;

      //  if (marker != null ? !marker.equals(that.marker) : that.marker != null) return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = marker != null ? marker.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
