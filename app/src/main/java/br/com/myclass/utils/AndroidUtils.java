package br.com.myclass.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.rey.material.widget.Button;

import java.io.File;

import br.com.myclass.R;

/**
 * Created by gilmar on 13/08/15.
 */
public class AndroidUtils {
    protected static final String TAG = "myclass";
    private static File file;


    public static void exibeDialog(final Activity context, final String mensagem) {
        try {
            final com.rey.material.app.Dialog dialog = new com.rey.material.app.Dialog(context);
            dialog.setContentView(R.layout.dialog_camera_galeria);
            dialog.setTitle(mensagem);
            final Button btnCamera = (Button) dialog.findViewById(R.id.btn_camera);
            final Button btnGaleria = (Button) dialog.findViewById(R.id.btn_galeria);
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Cria o caminho do arquivo no sdcard
                    file = SDCardUtils.getPrivateFile(context.getBaseContext(), System.currentTimeMillis() + ".jpg",
                            Environment.DIRECTORY_PICTURES);
                    // Chama a intent informando o arquivo para salvar a foto
                    Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    context.startActivityForResult(irParaCamera, 123);
                    dialog.dismiss();
                }
            });
            btnGaleria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    /*if (Build.VERSION.SDK_INT < 19) {
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                    } else {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                    }*/
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    context.startActivityForResult(intent, 2);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void alertDialog(final Context context, final String mensagem) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.app_name))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage(mensagem).create();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sim",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /* public  void hhh() {
        AlertDialog.Builder getImageFrom = new AlertDialog.Builder(getActivity());

        getImageFrom.setTitle("Select:");
        final CharSequence[] opsChars = {getResources().getString(R.string.takepic), getResources().getString(R.string.opengallery)};
        getImageFrom.setItems(opsChars, new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                } else if (which == 1) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            getResources().getString(R.string.pickgallery)), SELECT_PICTURE);
                }
                dialog.dismiss();
            }
        });
    }*/
}
