package com.fintech.ternaku.TernakPerah.DetailTernak.Task;

/**
 * Created by Pandhu on 10/26/16.
 */

public class ModelDetailTernakTask {
    String tgl_task_schedule;
    String isi_task;

    public ModelDetailTernakTask(){

    }

    public ModelDetailTernakTask(String tgl_task_schedule,String isi_task){
        this.setIsi_task(isi_task);
        this.setTgl_task_schedule(tgl_task_schedule);
    }

    public void setTgl_task_schedule(String tgl_task_schedule) {
        this.tgl_task_schedule = tgl_task_schedule;
    }

    public void setIsi_task(String isi_task) {
        this.isi_task = isi_task;
    }

    public String getTgl_task_schedule() {
        return tgl_task_schedule;
    }

    public String getIsi_task() {
        return isi_task;
    }
}
