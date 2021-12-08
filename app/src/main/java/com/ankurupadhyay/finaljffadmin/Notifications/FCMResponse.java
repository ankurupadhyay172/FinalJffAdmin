package com.ankurupadhyay.finaljffadmin.Notifications;
import java.util.List;

public class FCMResponse {


    private long multicast_id;
    private int success;
    private int failure;
    private int cononical_ids;
    private List<Result> results;

    public FCMResponse() {
    }


    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCononical_ids() {
        return cononical_ids;
    }

    public void setCononical_ids(int cononical_ids) {
        this.cononical_ids = cononical_ids;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}


class Result{
    private String message_id;

    public Result() {
    }


    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
