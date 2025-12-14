import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'firebase_options.dart';
void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('To-Do List')),
        body:  TodoApp(),
      ),
    );
  }
}

class TodoApp extends StatelessWidget {
   TodoApp({super.key});

  final CollectionReference _todosRef =
      FirebaseFirestore.instance.collection('todos');

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        children: [
          const _AddTodoForm(),
          const SizedBox(height: 20),
          Expanded(
            child: StreamBuilder<QuerySnapshot>(
              stream: _todosRef.snapshots(),
              builder: (context, snapshot) {
                if (!snapshot.hasData) {
                  return const Center(child: CircularProgressIndicator());
                }

                final todos = snapshot.data!.docs;
                return ListView.builder(
                  itemCount: todos.length,
                  itemBuilder: (context, index) {
                    final doc = todos[index];
                    final name = doc['name'] as String;
                    return ListTile(
                      title: Text(name),
                      leading: Checkbox(
                        value: false,
                        onChanged: (value) {
                          doc.reference.delete();
                        },
                      ),
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

class _AddTodoForm extends StatefulWidget {
  const _AddTodoForm();

  @override
  State<_AddTodoForm> createState() => _AddTodoFormState();
}

class _AddTodoFormState extends State<_AddTodoForm> {
  final TextEditingController _controller = TextEditingController();
  final CollectionReference _todosRef =
      FirebaseFirestore.instance.collection('todos');

  void _addTodo() {
    if (_controller.text.trim().isNotEmpty) {
      _todosRef.add({'name': _controller.text.trim()});
      _controller.clear();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: TextField(
            controller: _controller,
            decoration: const InputDecoration(
              hintText: ' Write your task here..w',
              border: OutlineInputBorder(),
            ),
          ),
        ),
        const SizedBox(width: 10),
        ElevatedButton(
          onPressed: _addTodo,
          child: const Text('Add'),
        ),
      ],
    );
  }
}