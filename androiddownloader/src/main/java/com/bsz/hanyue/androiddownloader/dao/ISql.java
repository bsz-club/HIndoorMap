package com.bsz.hanyue.androiddownloader.dao;

import com.bsz.hanyue.androiddownloader.DownloadTask;

import java.sql.SQLException;

/**
 * Created by hanyue on 2015/8/11
 */
public interface ISql {
    public void addDownloadTask(DownloadTask task) throws SQLException;

    public void updateDownloadTask(DownloadTask task) throws SQLException;

    public DownloadTask queryDownloadTask(DownloadTask task) throws SQLException;

    //public List<DownloadTask> queryDownloadTasksFromAlbum(Album album)throws SQLException;

    public void deleteDownloadTask(DownloadTask task) throws SQLException;
}
