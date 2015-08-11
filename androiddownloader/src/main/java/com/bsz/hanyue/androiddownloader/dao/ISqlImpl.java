package com.bsz.hanyue.androiddownloader.dao;

import android.content.Context;

import com.bsz.hanyue.androiddownloader.DownloadTask;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hanyue on 2015/8/11
 */
public class ISqlImpl implements ISql {
    private DatabaseHelper databaseHelper = null;

    private Context mContext = null;

    public ISqlImpl(Context context) {
        mContext = context;
    }

    /*
     * You'll need this in your class to release the helper when done.
     */
    public void Release() {

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    /**
     * You'll need this in your class to get the helper from the manager once
     * per class.
     */
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void addDownloadTask(DownloadTask task) throws SQLException {
        if (task == null) {
            return;
        }

        Dao<DownloadTask, Integer> taskDao = getHelper().getTaskDao();

        taskDao.createOrUpdate(task);
    }

    public void updateDownloadTask(DownloadTask task) throws SQLException {
        if (task == null) {
            return;
        }

        Dao<DownloadTask, Integer> taskDao = getHelper().getTaskDao();

        taskDao.update(task);
    }

    public DownloadTask queryDownloadTask(DownloadTask task) throws SQLException {
        List<DownloadTask> tasks = null;

        DownloadTask ttask = null;

        if (task == null) {
            return ttask;
        }

        Dao<DownloadTask, Integer> taskDao = getHelper().getTaskDao();

        tasks = taskDao.queryForEq("url", task.getUrl());

        if (tasks != null && tasks.size() > 0) {
            ttask = tasks.get(0);
        }

        return ttask;
    }

    public void deleteDownloadTask(DownloadTask task) throws SQLException {
        if (task == null) {
            return;
        }

        Dao<DownloadTask, Integer> taskDao = getHelper().getTaskDao();

        taskDao.delete(task);
    }

/*    @Override
    public List<DownloadTask> queryDownloadTasksFromAlbum(Album album) throws SQLException {
        List<DownloadTask> list = null;
        Album talbum = null;

        if (album == null) {
            return list;
        }

        Dao<Album, String> albumDao = getHelper().getAlbumDao();

        talbum = albumDao.queryForSameId(album);

        if (talbum == null) {
            return list;
        }

        list = new ArrayList<DownloadTask>();

        for (DownloadTask task : talbum.getDownloadTasks()) {
            list.add(task);
        }

        return list;
    }

    @Override
    public List<DownloadTask> queryDownloadTasksFromAlbums(Albums albums) throws SQLException {
        List<DownloadTask> list = null;
        Albums talbums = null;

        if (albums == null) {
            return list;
        }

        Dao<Albums, String> albumsDao = getHelper().getAlbumsDao();

        talbums = albumsDao.queryForSameId(albums);

        if (talbums == null) {
            return list;
        }

        list = new ArrayList<DownloadTask>();

        for (Album album : talbums.getAlbums()) {
            List<DownloadTask> alist = queryDownloadTasksFromAlbum(album);

            if (alist != null) {
                list.addAll(alist);
            }
        }

        return list;
    }*/

}