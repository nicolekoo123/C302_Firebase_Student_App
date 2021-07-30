package sg.edu.rp.c346.id19047433.firebasestudentapp;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView lvStudent;
    private ArrayList<Student> alStudent;
    private ArrayAdapter<Student> aaStudent;

    // TODO: Task 1 - Declare Firebase variables
    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvStudent = (ListView)findViewById(R.id.listViewStudents);

        // TODO: Task 2: Get FirebaseFirestore instance and reference
        db = FirebaseFirestore.getInstance();

        colRef = db.collection("students");
        docRef = colRef.document("students");

        alStudent = new ArrayList<Student>();
        aaStudent = new ArrayAdapter<Student>(getApplicationContext(), android.R.layout.simple_list_item_1, alStudent);
        lvStudent.setAdapter(aaStudent);

        //TODO: Task 3: Get real time updates from firestore by listening to collection "students"
        db.collection("students")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        alStudent.clear();
                        //TODO: Task 4: Read from Snapshot and add into ArrayAdapter for ListView
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("name") != null && doc.get("age") != null) {
                                alStudent.add(new Student(doc.getId(),doc.getString("name"), Integer.parseInt(doc.getLong("age").toString())));
                            }
                        }
                        Log.d(TAG, "Students: " + alStudent);
                        aaStudent.notifyDataSetChanged();
                    }
                });


        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student student = alStudent.get(i);  // Get the selected Student
                Intent intent = new Intent(MainActivity.this, StudentDetailsActivity.class);
                intent.putExtra("StudentID", student.getId());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.addStudent) {

            Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
