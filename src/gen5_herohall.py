# Añade a los héroes de la aldea el nivel de Consejo heroico que piden (hh por paso) y el que los desbloquea (hu)
import json
d=json.load(open('/home/claude/ck/assets/static_data.json')); c=json.load(open('catalog.json'))
for x in d['heroes']:
  k=str(x['_id'])
  if x.get('village')!='home' or k not in c: continue
  hh=[l.get('required_hero_tavern_level') or 0 for l in x['levels']]
  c[k]['hh']=hh[:len(c[k]['d'])]; c[k]['hu']=x['production_building_level']
json.dump(c,open('catalog.json','w'),ensure_ascii=False,separators=(',',':'))
