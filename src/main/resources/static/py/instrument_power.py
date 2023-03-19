import json
import sys
import numpy as np
from sklearn.cluster import KMeans
from sklearn.metrics import silhouette_score

if __name__ == '__main__':
    # df = pd.read_excel('C:/Users/michaeli/Downloads/test.xlsx', usecols=[1]) # code for test
    powerList = json.loads(sys.argv[1])
    result = []
    for item in powerList:
        result.append(float(item))
    x = np.array(result).reshape(-1, 1)
    # 定义候选的K值。
    scope = range(2, 8)
    # 定义SSE列表，用来存放不同K值下的SSE。
    #-----------------------------轮廓系数法确定k值（3）---------------
    scores = []
    n_clusters = 2
    for k in scope:
        kmeans = KMeans(n_init='auto', n_clusters=k,random_state=0).fit(x)
        value = silhouette_score(x, kmeans.labels_)
        scores.append(value)

    k = scores.index(max(scores)) + 2
    model = KMeans(n_init="auto", n_clusters=k, max_iter=100)
    model.fit(x)
    # 质心数值，确定档位
    c = np.sort(model.cluster_centers_.round(0).flatten()).tolist()
    print(c)