provider "linode" {
  token = "TBC"
}

resource "linode_lke_cluster" "terraform-kubernetes" {
  label       = "Terraform-Web-Example"
  k8s_version = "1.20"
  region      = "eu-west"
  tags        = ["test"]
  pool {
    type  = "g6-standard-4"
    count = 3
  }
}

//Export this cluster's attributes
output "kubeconfig" {
   value = base64decode(linode_lke_cluster.terraform-kubernetes.kubeconfig)
  sensitive = true
}

output "api_endpoints" {
   value = linode_lke_cluster.terraform-kubernetes.api_endpoints
}

output "status" {
   value = linode_lke_cluster.terraform-kubernetes.status
}

output "id" {
   value = linode_lke_cluster.terraform-kubernetes.id
}

output "pool" {
   value = linode_lke_cluster.terraform-kubernetes.pool
}
 
//resource "null_resource" "script"{
// provisioner "local-exec" {
//   command = "export KUBE_VAR=`terraform output kubeconfig` && echo $KUBE_VAR | base64 -d > lke-cluster-config.yaml"
//  }
//}

resource "local_file" "cubeconfig" {
  content  = base64decode(linode_lke_cluster.terraform-kubernetes.kubeconfig)
  filename = "configuracion.yaml"
  depends_on = [ linode_lke_cluster.terraform-kubernetes , ]
}

provider "helm" {
  kubernetes {
    config_path = "configuracion.yaml"
  }
}


resource helm_release elasticsearch {
  name       = "elasticsearch"

  repository = "https://helm.elastic.co"
  chart      = "elasticsearch"

  timeout    = 600000
  wait       = true

  depends_on = [
    local_file.cubeconfig,
  ]
}

resource helm_release kibana {
  name       = "kibana"
  repository = "https://Helm.elastic.co"
  chart      = "kibana"
  timeout    = 600000
  wait       = true
  depends_on = [ helm_release.elasticsearch ]
}


resource helm_release metricbeat {
  name       = "metricbeat"
  repository = "https://Helm.elastic.co"
  chart      = "metricbeat"
  depends_on = [ helm_release.elasticsearch ]
}

provider "kubernetes" {
  config_path = "configuracion.yaml"
}

resource "kubernetes_service" "kibana_web" {
  metadata {
    name = "kibana-web"
  }
  spec {
    selector =  {
      app = "kibana"
    }
    session_affinity = "ClientIP"
    port {
      port        = 80
      target_port = 5601
    }
    type = "LoadBalancer"
  }

  depends_on = [ helm_release.kibana, ]
}



