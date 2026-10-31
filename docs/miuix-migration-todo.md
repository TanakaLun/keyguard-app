# Miuix migration — status

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
- [x] Navigation transition/effects: `MiuixDefault` + `NavDisplayEffects(rememberNavSystemCornerRadius, …)`
- [x] Pull-to-search disabled
- [x] Generator: whole configuration in one `Card`
- [x] Generator tip: standard `BasicComponent` (icon in `startAction`)
- [x] Search field: Miuix `SearchBar` container

## Remaining

- [ ] Form/list screens still render plain rows without a `Card` around a group:
      accounts, sync, export, attachments, directory pages
- [ ] Replace the legacy row primitives with `BasicComponent` based rows and delete
      `FlatItemLayout`, `FlatItemLayoutExpressive`, `FlatDropdownSimpleExpressive`
      (note: `FlatItemLayoutExpressive` carries `onLongClick`, which `BasicComponent`
      does not expose — keep the long-press behaviour when migrating)
