# Miuix migration — remaining work

## Done

- [x] Theme: Miuix palette is the only color source, Material 3 scheme derived from it
- [x] Page/container colors: `surface` page, `surfaceContainer` cards, flat (no tonal elevation)
- [x] Root scaffold hosts all Miuix overlays (popups, dialogs)
- [x] Rows: plain, 16dp insets, no per-row shape/elevation
- [x] Section headers: `SmallTitle`
- [x] Dropdown menus: `OverlayListPopup` + `ListPopupColumn` + `DropdownImpl` row spec
- [x] Disclosure arrow: `MiuixIcons.Basic.ArrowRight`
- [x] Settings root + sub-pages: `SmallTitle` + `Card` + `BasicComponent`
- [x] Vault list + vault item detail: `SmallTitle` + `Card`
- [x] Navigation bar belongs to the stack root screen, no navigation animation
- [x] Navigation transition/effects: `MiuixDefault` + `NavDisplayEffects`
- [x] Pull-to-search disabled

## Next

- [ ] Generator: group the generation behaviour rows (checkboxes) into one `Card`
- [ ] Vault: both `ArrowPreference`s belong to one `Card`
- [ ] Description/explanation rows: `BasicComponent` with the icon in `startAction`, standard icon size
- [ ] Search: use the Miuix `SearchBar` for the in-page search fields
- [ ] Remaining form/list screens: group rows into `Card`s (accounts, sync, export,
      attachments, directories)
- [ ] Delete the legacy row primitives after the migration:
      `FlatItemLayout`, `FlatItemLayoutExpressive`, `FlatDropdownSimpleExpressive`
