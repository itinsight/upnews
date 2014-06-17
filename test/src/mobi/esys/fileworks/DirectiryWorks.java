package mobi.esys.fileworks;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mobi.esys.constants.K2Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

public class DirectiryWorks {
	private transient String directoryPath;
	private static final String DIR_WORKS_TAG = "DirectoryWorks";
	private transient List<String> stringArray;

	public DirectiryWorks(Context context, String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public void createDir() {
		final File videoDir = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ this.directoryPath);
		if (!videoDir.exists()) {
			videoDir.mkdirs();
		}
	}

	public String[] getDirFileList() {
		File videoDir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + this.directoryPath);
		Log.d(DIR_WORKS_TAG, videoDir.getAbsolutePath());
		final List<String> filePaths = new ArrayList<String>();
		if (videoDir.exists()) {
			final File[] files = videoDir.listFiles();
			for (final File file : files) {
				filePaths.add(file.getPath());
			}
		} else {
			Log.d(DIR_WORKS_TAG, "folder don't exist");
		}

		return filePaths.toArray(new String[filePaths.size()]);
	}

	public void deleteFilesFromDir(final List<Integer> maskList,
			final Context context) {
		String vfn = context.getSharedPreferences(K2Constants.APP_PREF,
				Context.MODE_PRIVATE).getString("videoFilesNames", "");
		Log.d("files", vfn);
		String[] videoFiles = vfn.split(",");
		stringArray = new ArrayList<String>(Arrays.asList(videoFiles));
		final File videoDir = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ this.directoryPath);
		Log.d(DIR_WORKS_TAG, "deleteFilesFromDir");
		Log.d(DIR_WORKS_TAG, Environment.getExternalStorageDirectory()
				.getAbsolutePath() + this.directoryPath);
		if (videoDir.exists()) {
			int ci = context.getSharedPreferences(K2Constants.APP_PREF,
					Context.MODE_PRIVATE).getInt("currPlIndex", 0);

			File[] files = videoDir.listFiles();

			if (maskList.size() == 1 && maskList.get(0) == 0) {
				for (int i = 0; i < files.length; i++) {

					if (((!files[i].getName().startsWith(
							K2Constants.FILE_PREFIX)) && ci != i)
							&& files[i].exists()) {
						files[i].delete();
						for (int j = 1; j < stringArray.size(); j++) {
							stringArray.remove(j);
						}
					}
				}
			}

			else {

				for (int i = 0; i < files.length; i++) {

					if (maskList.contains(i)) {

						if (((!files[i].getName().startsWith(
								K2Constants.FILE_PREFIX)) && ci != i)
								&& files[i].exists()) {
							files[i].delete();
							for (int j = 0; j < stringArray.size(); j++) {
								if (stringArray.get(j).equals(files[i])) {
									stringArray.remove(j);
								}
							}
						}
					}
				}
			}
			final SharedPreferences.Editor editor = context
					.getSharedPreferences(K2Constants.APP_PREF,
							Context.MODE_PRIVATE).edit();
			editor.putBoolean("isDeleting", false);
			editor.commit();
		} else {
			Log.d(DIR_WORKS_TAG, "Folder don't exists");
		}
	}

	public String[] getMD5Sums() {
		final String[] files = getDirFileList();
		String[] dirMD5s = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			final FileWorks fileWorks = new FileWorks(files[i]);
			dirMD5s[i] = fileWorks.getFileMD5();
		}
		return dirMD5s;
	}

	public boolean contains(final int[] mask, final int i) {
		boolean isCont = false;
		for (final int e : mask) {

			if (e == i || i == e) {

				isCont = true;
			} else {
				isCont = false;
			}

		}
		Log.d(DIR_WORKS_TAG, String.valueOf(isCont));
		return isCont;

	}
}
