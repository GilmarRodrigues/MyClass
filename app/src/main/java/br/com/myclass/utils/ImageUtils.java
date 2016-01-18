package br.com.myclass.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import br.com.myclass.helper.AlunoHelper;
import br.com.myclass.model.Aluno;

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getName();

    public static Bitmap getBitmapFromUri(Uri uri, Activity activity) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = activity.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

    public static Bitmap getBitmapFromUri(Uri uri, Activity activity, int width, int height) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = activity.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();


        // Configura o BitmapFactory para apenas ler o tamanho da imagem (sem carregá-la em memória)
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        // Faz o decode da imagem
        BitmapFactory.decodeFileDescriptor(fileDescriptor, new Rect(-1, -1, -1, -1), opts);
        // Lê a largura e altura do arquivo
        int w = opts.outWidth;
        int h = opts.outHeight;

        if (width == 0 || height == 0) {
            width = w / 8;
            height = h / 8;
        }

        Log.d(TAG, "Galeria Resize img, w:" + w + " / h:" + h + ", to w:" + width + " / h:" + height);

        // Fator de escala
        int scaleFactor = Math.min(w / width, h / height);
        opts.inSampleSize = scaleFactor;
        Log.d(TAG, "inSampleSize:" + opts.inSampleSize);
        // Agora deixa carregar o bitmap completo
        opts.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, new Rect(-1, -1, -1, -1), opts);
        Log.d(TAG, "Galeria Resize OK, w:" + bitmap.getWidth() + " / h:" + bitmap.getHeight());

        parcelFileDescriptor.close();
        return bitmap;
    }

    public static Bitmap getResizedImage(Uri uriFile, int width, int height, boolean fixMatrix) {
        try {
            // Configura o BitmapFactory para apenas ler o tamanho da imagem (sem carregá-la em memória)
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // Faz o decode da imagem
            BitmapFactory.decodeFile(uriFile.getPath(), opts);
            // Lê a largura e altura do arquivo
            int w = opts.outWidth;
            int h = opts.outHeight;

            if (width == 0 || height == 0) {
                width = w / 8;
                height = h / 8;
            }

            Log.d(TAG, "Resize img, w:" + w + " / h:" + h + ", to w:" + width + " / h:" + height);

            // Fator de escala
            int scaleFactor = Math.min(w / width, h / height);
            opts.inSampleSize = scaleFactor;
            Log.d(TAG, "inSampleSize:" + opts.inSampleSize);
            // Agora deixa carregar o bitmap completo
            opts.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(uriFile.getPath(), opts);

            Log.d(TAG, "Resize OK, w:" + bitmap.getWidth() + " / h:" + bitmap.getHeight());

            if (fixMatrix) {
                Bitmap newBitmap = fixMatrix(uriFile, bitmap);

                bitmap.recycle();

                return newBitmap;
            } else {
                return bitmap;
            }

        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static Bitmap getResizedImage(Uri uriFile, int width, int height) {
        try {
            // Carrega a imagem original em memória
            Bitmap bitmap = BitmapFactory.decodeFile(uriFile.getPath());

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            float scaleX = (float) width / bitmap.getWidth();
            float scaleY = (float) height / bitmap.getHeight();

            Matrix matrix = new Matrix();
            matrix.setScale(scaleX, scaleY);

            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

            resizedBitmap = fixMatrix(uriFile, resizedBitmap);

            // Limpa a memória do arquivo original (o grande que fizemos resize)
            bitmap.recycle();
            bitmap = null;

            // Retorna a imagem com o resize
            return resizedBitmap;
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static Bitmap getResizedImage2(Uri uriFile, int width, int height) {
        try {
            // Configura o BitmapFactory para apenas ler o tamanho da imagem (sem carregá-la em memória)
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // Faz o decode da imagem
            BitmapFactory.decodeFile(uriFile.getPath(), opts);
            // Lê a largura e altura do arquivo
            int w = opts.outWidth;
            int h = opts.outHeight;

            // Fator de escala
            opts.inSampleSize = Math.min(w / width, h / height);
            // Agora deixa carregar o bitmap completo
            opts.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(uriFile.getPath(), opts);

            Bitmap newBitmap = fixMatrix(uriFile, bitmap);

            bitmap.recycle();

            return newBitmap;
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private static Bitmap fixMatrix(Uri uriFile, Bitmap bitmap) throws IOException {
        Matrix matrix = new Matrix();

        /**
         * Classe para ler tags escritas no JPEG
         * Para utilizar esta classe precisa de Android 2.2 ou superior
         */
        ExifInterface exif = new ExifInterface(uriFile.getPath());

        // Lê a orientação que foi salva a foto
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        boolean fix = false;

        // Rotate bitmap
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                fix = true;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                fix = true;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                fix = true;
                break;
            default:
                // ORIENTATION_ROTATE_0
                fix = false;
                break;
        }

        if (!fix) {
            return bitmap;
        }

        // Corrige a orientação (passa a matrix)
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        bitmap.recycle();
        bitmap = null;

        return newBitmap;
    }

    // carrega img pelo helper do aluno
    public static void carregaImagemCamera(File caminhoArquivo, AlunoHelper helper, int w, int h, ImageView img) {
        helper.mAluno.setGaleria(null);
        helper.mAluno.setFoto(caminhoArquivo.toString());
        Bitmap bitmap = ImageUtils.getResizedImage(Uri.fromFile(caminhoArquivo), w, h, false);//1600x900
        img.setImageBitmap(bitmap);
    }

    public static void carregaImagemGaleria(Uri uri, Activity context, AlunoHelper helper, ImageView img) throws IOException {
        helper.mAluno.setFoto(null);
        helper.mAluno.setGaleria(uri.toString());
        Bitmap bitmap = ImageUtils.getBitmapFromUri(uri, context);
        img.setImageBitmap(bitmap);
    }

    // carrega img pelo Aluno
    public static void carregaImagemCamera(File caminhoArquivo, Aluno aluno, int w, int h, ImageView img) {
        aluno.setGaleria(null);
        aluno.setFoto(caminhoArquivo.toString());
        Bitmap bitmap = ImageUtils.getResizedImage(Uri.fromFile(caminhoArquivo), w, h, false);//1600x900
        img.setImageBitmap(bitmap);
    }

    public static void carregaImagemGaleria(Uri uri, Activity context, Aluno aluno, ImageView img) throws IOException {
        aluno.setFoto(null);
        aluno.setGaleria(uri.toString());
        Bitmap bitmap = ImageUtils.getBitmapFromUri(uri, context);
        img.setImageBitmap(bitmap);
    }
}
