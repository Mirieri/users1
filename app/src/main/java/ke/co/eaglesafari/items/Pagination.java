package ke.co.eaglesafari.items;


public class Pagination {
    String total,
            per_page
            ,current_page
            ,last_page,
            next_page_url,
            prev_page_url,
            from,
            to
            ;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    @Override
    public String toString() {
        return "Pagination{" +
                "total='" + total + '\'' +
                ", per_page='" + per_page + '\'' +
                ", current_page='" + current_page + '\'' +
                ", last_page='" + last_page + '\'' +
                ", next_page_url='" + next_page_url + '\'' +
                ", prev_page_url='" + prev_page_url + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
