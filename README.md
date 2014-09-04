android-textviewj
=================

### TextViewJ.
TextViewJ is a library to implement a TextView component with full justification support. The reason is that native TextView only supports left, center and right aligment. 

![[TextViewJ Preview]](http://3.bp.blogspot.com/-oXJCOHfpW6I/VAezVd3CzdI/AAAAAAAAAi8/IxiV7BgU7yo/s1600/textviewj.png)

### Instructions

Write in your layout:

xmlns:textviewj="http://schemas.android.com/apk/res-auto"

Then, use:

  <com.jacr.textviewj.TextViewJ
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="your_text"
        />

For more customizing, you can specify spacing between text lines and number of words by line.

 <com.jacr.textviewj.TextViewJ
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        textviewj:lineSpacing="your_value_dp"
        textviewj:wordsByLine="your_value_integer"
        android:text="your_text"
        />


### Developed By
Jesús Castro - castrojr913@gmail.com

### License
```
Copyright 2014 Jesús Castro

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```