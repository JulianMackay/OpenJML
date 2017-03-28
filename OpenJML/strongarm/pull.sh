#!/usr/local/bin/bash

# pull down the CSVs

scp jls@crunch:"/home/jls/Projects/Strongarm/OpenJML/OpenJML/OpenJML/strongarm/*.csv" /Users/jls/Projects/Strongarm/OpenJML/OpenJML/OpenJML/strongarm/

# pull down the figures

scp -r jls@crunch:"/home/jls/Projects/Strongarm/OpenJML/OpenJML/OpenJML/strongarm/figures/" /Users/jls/Projects/Strongarm/OpenJML/OpenJML/OpenJML/strongarm/figures/    


# pull down the JML specifications 

rsync -zarv  --include "*/" --exclude="*" --include="*.jml" jls@crunch:/home/jls/Projects/Analysis /Users/jls/Projects/Analysis/
