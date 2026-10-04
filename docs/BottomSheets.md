# Bottom sheets

Every bottom sheet in the app uses the shared layout in
`ui/feature/common/sheet/SheetLayout.kt`, so they all look and behave the same.

## What it provides

- `SheetLayout(onClose, modifier, content)`: a drag handle centred at the top, with `content` below it.
  - The handle has a fixed size and margins. Clicking it calls `onClose`, which must hide the sheet.
  - It applies the navigation bar inset, so the sheet container must not.
  - Content padding (e.g. side margins) is up to the content itself.
- `SheetDefaults`: the shared container colour, content colour, shape and scrim colour.

## Using it

Style the sheet container with `SheetDefaults`, turn off any built-in handle, and put the content in `SheetLayout`.

Material 3 `ModalBottomSheet`:

```kotlin
val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
val scope = rememberCoroutineScope()
ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = SheetDefaults.containerColor,
    contentColor = SheetDefaults.contentColor,
    shape = SheetDefaults.shape,
    scrimColor = SheetDefaults.scrimColor,
    dragHandle = null,
    windowInsets = WindowInsets(0, 0, 0, 0), // SheetLayout handles the nav bar inset
) {
    SheetLayout(
        onClose = { scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() } },
    ) {
        // custom content
    }
}
```

Material 2 `ModalBottomSheetLayout`: pass `SheetDefaults` to `sheetBackgroundColor`, `sheetShape` and
`scrimColor`, put `SheetLayout` in `sheetContent`, and use `onClose = { scope.launch { sheetState.hide() } }`.

## Existing sheets

`DiaryDatePickerSheet`, `RecapInfoSheet`, `RecapShareSheet`, `HubSwitcherSheet` (hosted in `NormalRouter`).
