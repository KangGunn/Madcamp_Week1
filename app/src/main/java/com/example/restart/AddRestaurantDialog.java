package com.example.restart;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddRestaurantDialog extends DialogFragment {

    public interface AddRestaurantListener {
        void onRestaurantAdded(String name, String phone, String imageURL, String type);
    }

    private AddRestaurantListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() instanceof AddRestaurantListener) {
                listener = (AddRestaurantListener) getParentFragment();
            } else if (context instanceof AddRestaurantListener) {
                listener = (AddRestaurantListener) context;
            } else {
                throw new ClassCastException("Parent must implement AddRestaurantListener");
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddRestaurantListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // 커스텀 레이아웃 설정
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_restaurant, null);

        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editPhone = dialogView.findViewById(R.id.editPhone);
        RadioGroup radioGroupType = dialogView.findViewById(R.id.radioGroupType);

        builder.setView(dialogView)
                .setTitle("Add New Restaurant")
                .setPositiveButton("Add", (dialog, id) -> {
                    String name = editName.getText().toString().trim();
                    String phone = editPhone.getText().toString().trim();
                    String type = "";

                    // RadioGroup에서 선택한 타입 가져오기
                    int selectedId = radioGroupType.getCheckedRadioButtonId();
                    if (selectedId != -1) {
                        RadioButton selectedRadioButton = dialogView.findViewById(selectedId);
                        type = selectedRadioButton.getText().toString();
                    }

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(type)) {
                        Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                    } else {
                        listener.onRestaurantAdded(name, phone, "1", type);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }
}
