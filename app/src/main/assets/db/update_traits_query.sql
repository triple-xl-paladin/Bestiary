update traits
set trait_sort_order = 68
where trait_text in (select trait_6_text_8 from monster where trait_6_text_8 is not null)
and trait_name in (select trait_6_name from monster where trait_6_name is not null)
and monster_id in (select monster_id from monster)