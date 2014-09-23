AndroidBinding
==============

MVVM for Android

egandro branch:

- TreeView Widget
- BreadCrum Widget
- TextView/EditView can contain SpannableStrings

09.23.2014

- added unbinding
- added automatic unbinding for BindableLayouts
- some major fixes for the WeakList
- under rare cicrumstances (race condidions, thread concurancy with the UI and a GC call) we had null pointer references in the unsubscribes()
-
